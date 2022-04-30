package com.evaluator.parser;

import com.evaluator.operators.Operator;
import com.evaluator.parser.exceptions.*;

import com.evaluator.types.exceptions.DivisionByZeroException;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;
import com.evaluator.types.exceptions.NegativeValueException;
import com.evaluator.values.Value;
import com.evaluator.values.ValueType;
import com.evaluator.tokens.Token;
import com.evaluator.tokens.TokenType;

import com.evaluator.types.BigInt;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Collections;
import java.util.Stack;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    public static final int DEFAULT_PRECISION = 5;

    public static final String DEFAULT_SPLIT_CHARACTER = ";";
    public static final String SPLIT_PATTERN = "(?=([^\\\"\\']*[\\\"\\'][^\\\"\\']*[\\\"\\'])*[^\\\"\\']*$)";

    private int precision = DEFAULT_PRECISION;

    private Mode mode = Mode.INTERACTIVE;

    private boolean sensitive;

    private Pattern combinedPattern;

    private final String delimiter;

    final Map < String, List < Token >> expTokens = new HashMap < > ();

    private final Map<String, BigInt> constants = new HashMap<String, BigInt>();
    private final Map < String, Value > variables = new TreeMap < > ();

    public Parser() {
        sensitive = false;
        delimiter = DEFAULT_SPLIT_CHARACTER;
        clearConstants();
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void clearConstants() {
        constants.clear();
        invalidatePattern();
    }

    public BigInt getConstant(String name) {
        return name == null ? null : constants.get(sensitive ? name : name.toUpperCase());
    }

    public String getConstantRegex() {
        List < String > names = new ArrayList < > (constants.keySet());
        names.sort(Collections. < String > reverseOrder());

        StringBuilder sb = new StringBuilder();
        for (String name: names) {
            if (sb.length() > 0) {
                sb.append("|");
            }
            sb.append(name);
        }

        if (sb.length() == 0) {
            sb.append("~~no-constants-defined~~");
        }

        return sb.toString();
    }

    public Pattern getPattern(Parser parser) {
        if (combinedPattern == null) {
            StringBuilder sb = new StringBuilder();
            for (TokenType type: TokenType.values()) {
                sb.append(String.format("|(?<%s>%s)", type.name(), type.getRegex(parser)));
            }

            int options = parser.getCaseSensitive() ?
                    Pattern.UNICODE_CASE : Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE;

            options |= Pattern.UNICODE_CHARACTER_CLASS;

            combinedPattern = Pattern.compile(sb.substring(1), options);
        }
        return combinedPattern;
    }

    public void invalidatePattern() {
        combinedPattern = null;
    }

    public void addVar(String name, Value value) {
        if (name != null && value != null) {
            variables.put(sensitive ? name : name.toUpperCase(), value);
        }
    }

    public void clearVar(String name) {
        variables.remove(sensitive ? name : name.toUpperCase());
    }

    public void clearVars() {
        variables.clear();
    }

    public Value getVar(String name) {
        return name == null ? null : variables.get(sensitive ? name : name.toUpperCase());
    }

    public Map < String, Value > getVars() {
        return variables;
    }

    private boolean isType(Token token, int type) throws ParserException {
        if (!token.isOperator()) {
            throw new ExpectedOperatorException(token.getRow(), token.getColumn());
        }

        Operator o = Operator.find(token);
        if (o == null) {
            throw new OperatorNotFoundException(token.getRow(), token.getColumn());
        }

        return o.getAssociation() == type;
    }

    private boolean shouldPopToken(Token token, Token topOfStack, boolean caseSensitive) throws ParserException {
        Operator op = Operator.find(token);
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

    public void clearCache() {
        expTokens.clear();
    }

    public boolean getCaseSensitive() {
        return sensitive;
    }

    public boolean setCaseSensitive(boolean caseSensitive) {
        boolean oldValue = this.sensitive;
        this.sensitive = caseSensitive;
        return oldValue;
    }

    public int getPrecision() {
        return precision;
    }

    public int setPrecision(int decimals) {
        int oldValue = this.precision;
        this.precision = decimals;
        return oldValue;
    }

    public Value evaluate(String source)
            throws ParserException,
            InvalidNumberFormatException,
            MaximumNumberOfDecimalExceededException,
            NegativeValueException,
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

            // Check for invalid tokens in the expression
            for (Token token: tokens) {
                if (TokenType.NOMATCH.equals(token.getType())) {
                    throw new InvalidTokenException(token.getRow(), token.getColumn());
                }
            }
        }

        return tokens;
    }

    public List <Token> generateRPN(List < Token > inputTokens) throws ParserException {
        List < Token > outputTokens = new ArrayList < > ();
        Stack < Token > stack = new Stack < > ();

        int count = 0;
        for (Token token: inputTokens) {

            if (token.operatorEquals(Operator.L_PARENTHESIS)) {
                count++;
            } else if (token.operatorEquals(Operator.R_PARENTHESIS)) {
                count--;
                if (count < 0) {
                    throw new MissingParenthesisException(token.getRow(), token.getColumn());
                }
            }
        }

        if (count != 0) {
            Token token = inputTokens.get(inputTokens.size() - 1);
            throw new MissingParenthesisException(token.getRow(), token.getRow());
        }

        for (Token token: inputTokens) {

            if (!token.isOperator() &&
                    (token.isNumber() || token.isConstant() || token.isIdentifier())) {
                outputTokens.add(token);
            } else if (token.operatorEquals(Operator.L_PARENTHESIS)) {
                stack.push(token);
            } else if (token.operatorEquals(Operator.R_PARENTHESIS)) {
                while (!stack.empty() && !stack.peek().operatorEquals(Operator.L_PARENTHESIS)) {
                    outputTokens.add(stack.pop());
                }

                stack.pop();

            } else if (token.isOperator()) {
                // While stack not empty and stack top element is an operator add it to the output
                while (!stack.empty() && stack.peek().isOperator()) {
                    if (shouldPopToken(token, stack.peek(), sensitive)) {
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

    private void assertBothNumbers(Token lhs, Token rhs) throws ParserException {
        if (lhs.getValue().getType() != ValueType.NUMBER || rhs.getValue().getType() != ValueType.NUMBER) {
            throw new IncompatibleTypesException(lhs.getRow(), lhs.getColumn());

        }
    }

    private void assertSufficientStack(Token token, Stack < Token > stack) throws ParserException {
        if (stack.size() < 2) {
            throw new IncompatibleTypesException(token.getRow(), token.getColumn());
        }
    }

    private Token processOperators(Token token, Stack < Token > stack)
            throws ParserException, InvalidNumberFormatException, MaximumNumberOfDecimalExceededException, DivisionByZeroException {
        Token result = null;

        Operator op = Operator.find(token);

        assertSufficientStack(token, stack);
        Token rhs = stack.pop();
        Token lhs = stack.pop();

        try {
            if (op.equals(Operator.PLUS)) {
                // Addition
                assertBothNumbers(lhs, rhs);
                BigInt bi = lhs.asNumber().add(rhs.asNumber());
                result = new Token(TokenType.NUMBER, bi.convertToString(), token.getRow(), token.getColumn());

            } else if (op.equals(Operator.MINUS)) {
                // Subtraction
                assertBothNumbers(lhs, rhs);
                BigInt bi = lhs.asNumber().subtract(rhs.asNumber());
                result = new Token(TokenType.NUMBER, bi.convertToString(), token.getRow(), token.getColumn());

            } else if (op.equals(Operator.MULT)) {
                // Multiplication
                assertBothNumbers(lhs, rhs);
                BigInt bi = lhs.asNumber().multiply(rhs.asNumber());
                result = new Token(TokenType.NUMBER, bi.convertToString(), token.getRow(), token.getColumn());

            } else if (op.equals(Operator.DIV)) {
                // Division
                assertBothNumbers(lhs, rhs);
                BigInt bi = lhs.asNumber().divide(rhs.asNumber());
                result = new Token(TokenType.NUMBER, bi.convertToString(), token.getRow(), token.getColumn());

            } else if (op.equals(Operator.ASSIGNMENT)) {
                // Assignment
                if (lhs.isIdentifier()) {
                    // Trying to assign an uninitialized variable -- could also get here
                    if (rhs.getValue().getType().equals(ValueType.UNDEFINED)) {
                        throw new ExpectedInitializationException(rhs.getRow(), rhs.getColumn());
                    }

                } else {
                    throw new ExpectedInitializationException(token.getRow(), token.getColumn());
                }
                Value value = variables.get(sensitive ? lhs.getText() : lhs.getText().toUpperCase());
                value.set(rhs.getValue());
            }
        } catch (ArithmeticException ex) {
            throw new ParserException(ex.getMessage(), ex, token.getRow(), token.getColumn());
        }

        return result;
    }

    public Value computeRPN(List < Token > tokens)
            throws ParserException, InvalidNumberFormatException, MaximumNumberOfDecimalExceededException, NegativeValueException, DivisionByZeroException {

        Stack < Token > stack = new Stack < > ();
        for (Token token: tokens) {
            if (token.isConstant()) {
                BigInt bd = getConstant(token.getText());
                token.getValue().setValue(bd);
                stack.push(token);
            } else if (token.isIdentifier()) {
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
            throw new SyntaxErrorException(stack.get(0).getRow(), stack.get(0).getColumn());
        }

        return stack.size() == 0 ? new Value("empty result") : stack.pop().getValue();

    }

}