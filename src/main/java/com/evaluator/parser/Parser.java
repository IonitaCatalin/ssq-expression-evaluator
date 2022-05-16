package com.evaluator.parser;

import com.evaluator.modes.Mode;
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

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Stack;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    public static final String DEFAULT_SPLIT_CHARACTER = ";";
    public static final String SPLIT_PATTERN = "(?=([^\\\"\\']*[\\\"\\'][^\\\"\\']*[\\\"\\'])*[^\\\"\\']*$)";

    private final Mode mode = Mode.INTERACTIVE;

    private final boolean sensitive;

    private Pattern pattern;

    private final String delimiter;

    final Map < String,List<Token>> expTokens = new HashMap<>();

    private final Map<String, Value> variables = new TreeMap<>();

    public Parser() {
        sensitive = false;
        delimiter = DEFAULT_SPLIT_CHARACTER;
        clearConstants();
    }

    public void clearConstants() {
        invalidatePattern();
    }


    public Pattern getPattern(Parser parser) {
        if (pattern == null) {
            StringBuilder sb = new StringBuilder();
            for (TokenType type: TokenType.values()) {
                sb.append(String.format("|(?<%s>%s)", type.name(), type.getRegex(parser)));
            }

            int options = parser.getCaseSensitive() ?
                    Pattern.UNICODE_CASE : Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE;

            options |= Pattern.UNICODE_CHARACTER_CLASS;

            pattern = Pattern.compile(sb.substring(1), options);
        }
        return pattern;
    }

    public void invalidatePattern() {
        pattern = null;
    }

    private boolean isType(Token token, int type) throws ParserException {
        if (!token.isOperator()) {
            throw new OperatorExpectedException(token.getRow(), token.getColumn());
        }

        Operator o = Operator.find(token);
        if (o == null) {
            throw new OperatorNotFoundException(token.getRow(), token.getColumn());
        }

        return o.getAssociation() == type;
    }

    private boolean shouldPopToken(Token token, Token topOfStack) throws ParserException {
        return (this.isType(token, Operator.LEFT_ASSOCIATIVE) && this.compareTokens(token, topOfStack) >= 0) ||
                    (this.isType(token, Operator.RIGHT_ASSOCIATIVE) && compareTokens(token, topOfStack) > 0);

    }

    private int compareTokens(Token token1, Token token2) throws ParserException {
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

        return o1.getPrecedence() - o2.getPrecedence();
    }

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

    public List < Token > tokenize(String input, boolean wantWhitespace)
            throws ParserException, InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {
        int offset = 0;
        int row = 1;

        List < Token > tokens = new ArrayList < > ();

        Matcher matcher = getPattern(this).matcher(input);
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

    public List <Token> generateRPN(List <Token> tokens) throws ParserException {

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

        return outputTokens;
    }


    private Token processOperators(Token token, Stack < Token > stack)
            throws ParserException, InvalidNumberFormatException, MaximumNumberOfDecimalExceededException, DivisionByZeroException {
        Token result = null;

        Operator op = Operator.find(token);

        Token rhs = stack.pop();
        Token lhs = stack.pop();

        try {
            if (op.equals(Operator.PLUS)) {
                BigInt bi = lhs.asNumber().add(rhs.asNumber());
                result = new Token(TokenType.NUMBER, bi.convertToString(), token.getRow(), token.getColumn());

            } else if (op.equals(Operator.MINUS)) {
                BigInt bi = lhs.asNumber().subtract(rhs.asNumber());
                result = new Token(TokenType.NUMBER, bi.convertToString(), token.getRow(), token.getColumn());

            } else if (op.equals(Operator.MULT)) {
                BigInt bi = lhs.asNumber().multiply(rhs.asNumber());
                result = new Token(TokenType.NUMBER, bi.convertToString(), token.getRow(), token.getColumn());

            } else if (op.equals(Operator.DIV)) {
                BigInt bi = lhs.asNumber().divide(rhs.asNumber());
                result = new Token(TokenType.NUMBER, bi.convertToString(), token.getRow(), token.getColumn());

            } else if(op.equals(Operator.POW)) {
                BigInt bi = lhs.asNumber().pow(rhs.asNumber());
                result = new Token(TokenType.NUMBER, bi.convertToString(), token.getRow(), token.getColumn());

            } else if(op.equals(Operator.SQRT)) {
                BigInt bi = lhs.asNumber().sqrt(rhs.asNumber());
                result = new Token(TokenType.NUMBER, bi.convertToString(), token.getRow(), token.getColumn());

            } else if (op.equals(Operator.ASSIGNMENT)) {
                if (lhs.isIdentifier()) {
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

    public Value computeRPN(List < Token > tokens)
            throws ParserException, InvalidNumberFormatException, MaximumNumberOfDecimalExceededException, DivisionByZeroException {

        Stack < Token > stack = new Stack < > ();
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

        return stack.size() == 0 ? new Value("empty result") : stack.pop().getValue();

    }


}