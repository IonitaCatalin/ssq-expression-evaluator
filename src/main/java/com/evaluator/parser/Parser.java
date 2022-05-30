package com.evaluator.parser;

import com.evaluator.utils.Conditions;
import com.evaluator.operators.Operator;

import com.evaluator.parser.exceptions.BadSyntaxException;
import com.evaluator.parser.exceptions.InitializationExpectedException;
import com.evaluator.parser.exceptions.InvalidTokenException;
import com.evaluator.parser.exceptions.OperatorExpectedException;
import com.evaluator.parser.exceptions.OperatorNotFoundException;
import com.evaluator.parser.exceptions.ParenthesisCountException;
import com.evaluator.parser.exceptions.ParserException;
import com.evaluator.types.exceptions.NegativeValueException;

import com.evaluator.values.Value;
import com.evaluator.values.ValueType;
import com.evaluator.tokens.Token;
import com.evaluator.tokens.TokenType;

import com.evaluator.types.exceptions.DivisionByZeroException;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;

import com.evaluator.types.BigInt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Main Parser class
 *
 * @author Ionita Mihail-Catalin
 * @author Popa Stefan
 * @since 01.05.2022
 */

public class Parser {


    /**
     *  Default delimiter character for instructions
     */
    public static final String DEFAULT_SPLIT_CHARACTER = ";";

    /**
     *  Default escaped pattern
     */
    public static final String SPLIT_PATTERN = "(?=([^\\\"\\']*[\\\"\\'][^\\\"\\']*[\\\"\\'])*[^\\\"\\']*$)";

    /**
     * Boolean values which is true if the variables names
     * case-sensitive or not by default it would be false
     */
    private final boolean sensitive;

    /**
     * Pattern object
     */
    private Pattern pattern;

    /**
     * The character delimiter for instructions,
     * by default the ';' character is chosen
     */
    private final String delimiter;

    /**
     * The tokens from the expression in the process of being resolved
     */
    final Map<String,List<Token>> expTokens = new HashMap<>();

    /**
     * The Variables in found by the parser and their respective values
     */
    private Map<String, Value> variables = new TreeMap<>();

    public Parser() {
        sensitive = false;
        delimiter = DEFAULT_SPLIT_CHARACTER;
        clearConstants();
    }

    /**
     * Cleans the current variables in the parser
     */
    public void clearConstants() {
        invalidatePattern();
    }

    /**
     * Preprocess the pattern to be used for matching the tokens in the expression
     *
     * @param parser The parsers
     * @return The pattern used for matching the tokens
    */
    public Pattern getPattern(Parser parser) {
        if (pattern == null) {
            StringBuilder sb = new StringBuilder();

            assert Conditions.isOfSize(List.of(TokenType.values()), TokenType.values().length);

            for (TokenType type: TokenType.values()) {
                sb.append(String.format("|(?<%s>%s)", type.name(), type.getRegex()));
            }

            assert Conditions.isOfSize(List.of(TokenType.values()), TokenType.values().length);

            int options = parser.getCaseSensitive() ?
                    Pattern.UNICODE_CASE : Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE;

            options |= Pattern.UNICODE_CHARACTER_CLASS;

            pattern = Pattern.compile(sb.substring(1), options);
        }
        return pattern;
    }

    /**
     * Invalidate the pattern used for matching the tokens
     */
    public void invalidatePattern() {
        pattern = null;
    }

    /**
     * Check if a token is of indicated type
     *
     * @param token The token to be checked
     * @param type The type of the token
     * @return boolean value
     */
    public boolean isType(Token token, int type) throws ParserException {

        assert Conditions.isNotNull(token);

        if (!token.isOperator()) {
            throw new OperatorExpectedException(token.getRow(), token.getColumn());
        }

        Operator o = Operator.find(token);

        if (o == null) {
            throw new OperatorNotFoundException(token.getRow(), token.getColumn());
        }

        assert Conditions.isNotNull(o);

        return o.getAssociation() == type;
    }

    /**
     * Check if the token in the top of the stack of token of expression should be
     * removed, in general we pop of the token on top of the stack if that token
     * has the precedence lesser or equal to the current token in the expression
     * we are resolving
     *
     * @param token The token to be checked
     * @param topOfStack The token on top of the stack
     * @return boolean value
     */
    private boolean shouldPopToken(Token token, Token topOfStack) throws ParserException {

        // Preconditions
        assert Conditions.isNotNull(token);
        assert Conditions.isNotNull(topOfStack);

        return (this.isType(token, Operator.LEFT_ASSOCIATIVE) && this.compareTokens(token, topOfStack) >= 0) ||
                    (this.isType(token, Operator.RIGHT_ASSOCIATIVE) && compareTokens(token, topOfStack) > 0);

    }


    /**
     * Compare two sets of tokens, returns 0 if the tokens are equals, -1 if the precedence of
     * the first token is less than the second and 1 otherwise.
     *
     * @param token1 The token that's being compared
     * @param token2 The token that's compared against
     * @return int value
     */
    private int compareTokens(Token token1, Token token2) throws ParserException {

        assert Conditions.isNotNull(token1);
        assert Conditions.isNotNull(token2);

        if (!token1.isOperator()) {
            throw new OperatorExpectedException(token1.getRow(), token1.getColumn());
        } else if (!token2.isOperator()) {
            throw new OperatorExpectedException(token1.getRow(), token1.getColumn());
        }

        Operator o1 = Operator.find(token1);
        if (o1 == null) {
            throw new OperatorNotFoundException(token1.getRow(), token1.getColumn());
        }

        Operator o2 = Operator.find(token2);
        if (o2 == null) {
            throw new OperatorNotFoundException(token2.getRow(), token2.getColumn());
        }

        assert Conditions.isNotNull(o1);
        assert Conditions.isNotNull(o2);

        return o1.getPrecedence() - o2.getPrecedence();
    }


    /**
     * Checks if the sensitive flag is enabled or not
     */
    public boolean getCaseSensitive() {
        return sensitive;
    }


    public Value evaluate(String source)
            throws ParserException,
            InvalidNumberFormatException,
            MaximumNumberOfDecimalExceededException,
            DivisionByZeroException
    {
        if (source == null) {
            source = "";
        }
        source += ";";

        Value value = new Value("");
        String[] expressions = source.split(delimiter + SPLIT_PATTERN);

        for (String expression: expressions) {
            if (expression.trim().length() > 0) {
                List <Token> tokens = expTokens.get(expression);
                if (tokens == null) {
                    tokens = new ArrayList <> ();
                    List < Token > list = tokenize(expression, false);
                        if (list.size() > 0) {
                        tokens.addAll(generateRPN(list));
                        expTokens.put(expression, tokens);
                    }
                }
                value = (tokens.size() > 0) ? computeRPN(tokens) : new Value("ERROR: EMPTY EXPRESSION");
            }
        }

        return value;
    }

    /**
     * Matches every known token in the input
     *
     * @param input The input expression as a string
     * @param wantWhitespace Flag that indicates if whitespaces should be ignored or not
     * @return a list of tokens extracted from the expression
     * @throws InvalidTokenException If the token is not of any known type
     */
    public List<Token> tokenize(String input, boolean wantWhitespace)
            throws ParserException,
            InvalidNumberFormatException,
            MaximumNumberOfDecimalExceededException
    {

        int offset = 0;
        int row = 1;

        List <Token> tokens = new ArrayList<> ();
        Matcher matcher = getPattern(this).matcher(input);

        assert Conditions.isNotNull(matcher);

        while (matcher.find()) {
            if (wantWhitespace || matcher.group(TokenType.WHITESPACE.name()) == null) {
                for (TokenType tokenType: TokenType.values()) {
                    if (matcher.group(tokenType.name()) != null) {
                        String text = tokenType.resolve(matcher.group(tokenType.name()));
                        Token token = new Token(tokenType, text, row, matcher.start() + 1 - offset);
                        tokens.add(token);
                        break;
                    }
                }
            }

            if (matcher.group(TokenType.NEWLINE.name()) != null) {
                offset = matcher.start() + 1;
                row++;
            }
        }

        if (tokens.size() > 1) {
            int last = tokens.size() - 1;
            Token lastToken = tokens.get(last);
            if (TokenType.NOMATCH.equals(lastToken.getType())) {
                tokens.remove(last);
            }

            for (Token token: tokens) {
                if (TokenType.NOMATCH.equals(token.getType())) {
                    throw new InvalidTokenException(token.getRow(), token.getColumn());
                }
            }
        }

        return tokens;
    }

    /**
     * Generates the reverse polish notation of the expression
     *
     * @param tokens The tokens of the expression
     * @return A list of token in the reverse polish notation
     * @throws ParenthesisCountException If the number of opening parenthesis is greater
     * than the number of closing parenthesis or otherwise
     */
    public List<Token> generateRPN(List<Token> tokens) throws ParserException {

        assert Conditions.isNotNull(tokens);
        assert Conditions.hasNElements(Collections.singletonList(tokens), 1);

        List<Token> outputTokens = new ArrayList<>();
        Stack<Token> stack = new Stack<>();

        int count = 0;
        for (Token token: tokens) {
            if (token.operatorEquals(Operator.L_PARENTHESIS)) {
                count++;
            } else if (token.operatorEquals(Operator.R_PARENTHESIS)) {
                count--;
                if (count < 0) {
                    throw new ParenthesisCountException(token.getRow(), token.getColumn());
                }
            }
        }

        if (count != 0) {
            Token token = tokens.get(tokens.size() - 1);
            throw new ParenthesisCountException(token.getRow(), token.getRow());
        }

        for (Token token: tokens) {
            if (!token.isOperator() && (token.isNumber() || token.isIdentifier())) {
                outputTokens.add(token);
            } else if (token.operatorEquals(Operator.L_PARENTHESIS)) {
                stack.push(token);
            } else if (token.operatorEquals(Operator.R_PARENTHESIS)) {
                while (!stack.empty() && !stack.peek().operatorEquals(Operator.L_PARENTHESIS)) {
                    outputTokens.add(stack.pop());
                }

                stack.pop();

            } else if (token.isOperator()) {
                while (!stack.empty() && stack.peek().isOperator()) {
                    if (shouldPopToken(token, stack.peek())) {
                        outputTokens.add(stack.pop());
                        continue;
                    }
                    break;
                }
                stack.push(token);
            }
        }

        while (!stack.empty()) {
            outputTokens.add(stack.pop());
        }

        assert Conditions.isNotNull(outputTokens);

        return outputTokens;
    }

    /**
     * Processes an operator and its operands
     *
     * @param token The token to be processed
     * @return A token
     * @throws ParenthesisCountException If the number of opening parenthesis is greater
     * than the number of closing parenthesis or otherwise
     * @throws InitializationExpectedException If the token is a assignment operator but there is no right operand
     */
    public Token processOperators(Token token, Stack<Token> stack)
            throws ParserException,
            InvalidNumberFormatException,
            MaximumNumberOfDecimalExceededException,
            DivisionByZeroException
    {
        Token result = null;

        Operator op = Operator.find(token);

        Token rhs = stack.pop();
        Token lhs = stack.pop();

        try {
            if (op.equals(Operator.PLUS)) {
                assert Conditions.areOperandsNumeric(lhs, rhs);

                BigInt bi = lhs.asNumber().add(rhs.asNumber());
                result = new Token(TokenType.NUMBER, bi.convertToString(), token.getRow(), token.getColumn());

                assert Conditions.isTokenOfType(result, TokenType.NUMBER);

            } else if (op.equals(Operator.MINUS)) {
                assert Conditions.areOperandsNumeric(lhs, rhs);

                BigInt bi = lhs.asNumber().subtract(rhs.asNumber());
                result = new Token(TokenType.NUMBER, bi.convertToString(), token.getRow(), token.getColumn());

                assert Conditions.isTokenOfType(result, TokenType.NUMBER);

            } else if (op.equals(Operator.MULT)) {
                assert Conditions.areOperandsNumeric(lhs, rhs);

                BigInt bi = lhs.asNumber().multiply(rhs.asNumber());
                result = new Token(TokenType.NUMBER, bi.convertToString(), token.getRow(), token.getColumn());

                assert Conditions.isTokenOfType(result, TokenType.NUMBER);


            } else if (op.equals(Operator.DIV)) {
                assert Conditions.areOperandsNumeric(lhs, rhs);

                BigInt bi = lhs.asNumber().divide(rhs.asNumber());
                result = new Token(TokenType.NUMBER, bi.convertToString(), token.getRow(), token.getColumn());

                assert Conditions.isTokenOfType(result, TokenType.NUMBER);

            } else if(op.equals(Operator.POW)) {
                assert Conditions.areOperandsNumeric(lhs, rhs);

                BigInt bi = lhs.asNumber().pow(rhs.asNumber());
                result = new Token(TokenType.NUMBER, bi.convertToString(), token.getRow(), token.getColumn());

                assert Conditions.isTokenOfType(result, TokenType.NUMBER);

            } else if(op.equals(Operator.SQRT)) {
                assert Conditions.areOperandsNumeric(lhs, rhs);

                BigInt bi = lhs.asNumber().sqrt(rhs.asNumber());
                result = new Token(TokenType.NUMBER, bi.convertToString(), token.getRow(), token.getColumn());

                assert Conditions.isTokenOfType(result, TokenType.NUMBER);

            } else if (op.equals(Operator.ASSIGNMENT)) {
                if (lhs.isIdentifier()) {
                    assert Conditions.isStackSizeSufficient(stack, 1);
                    if (rhs.getValue().getType().equals(ValueType.UNDEFINED)) {
                        throw new InitializationExpectedException(rhs.getRow(), rhs.getColumn());
                    }
                } else {
                    throw new InitializationExpectedException(token.getRow(), token.getColumn());
                }
                Value value = variables.get(sensitive ? lhs.getText() : lhs.getText().toUpperCase());
                value.set(rhs.getValue());

            }
        } catch (ArithmeticException | NegativeValueException ex) {
            throw new ParserException(ex.getMessage(), ex, token.getRow(), token.getColumn());
        }

        return result;
    }

    /**
     * Computes the reverse polish notation to obtain a value of the tokenized expression
     *
     * @param tokens A list of tokens in the reverse polish notation
     * @return A value
     * @throws BadSyntaxException If after the processing of the reverse polish notation
     * there are tokens left in the stack
     */
    public Value computeRPN(List<Token> tokens)
            throws ParserException,
            InvalidNumberFormatException,
            MaximumNumberOfDecimalExceededException,
            DivisionByZeroException
    {

        assert Conditions.isNotNull(tokens);

        Stack <Token> stack = new Stack < > ();
        for (Token token: tokens) {
            if (token.isIdentifier()) {
                Value value = variables.get(sensitive ? token.getText() : token.getText().toUpperCase());

                if (value == null) {
                    value = new Value();
                    value.set(token.getValue());
                    variables.put(sensitive ? token.getText() : token.getText().toUpperCase(), value);
                }

                token.getValue().set(value);
                stack.push(token);
            } else if (token.isOperator()) {
                Token result = processOperators(token, stack);
                if (result != null) {
                    stack.push(result);
                }
            } else {
                stack.push(token);
            }
        }

        if (stack.size() > 1) {
            throw new BadSyntaxException(stack.get(0).getRow(), stack.get(0).getColumn());
        }

        assert Conditions.hasNElements(Collections.singletonList(stack), 1);
        return stack.size() == 0 ? new Value("empty result") : stack.pop().getValue();

    }


}