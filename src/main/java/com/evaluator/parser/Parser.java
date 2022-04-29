package com.evaluator.parser;

import com.evaluator.tokenizer.Operator;
import com.evaluator.exceptions.ParserException;
import com.evaluator.tokenizer.Value;
import com.evaluator.tokenizer.ValueType;
import com.evaluator.tokenizer.Token;
import com.evaluator.tokenizer.TokenType;

import java.math.BigDecimal;
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

    private static final String DEFAULT_SPLIT_CHARACTER = ";";
    private static final String SPLIT_REGEX = "(?=([^\\\"\\']*[\\\"\\'][^\\\"\\']*[\\\"\\'])*[^\\\"\\']*$)";

    private int precision = DEFAULT_PRECISION;

    private boolean sensitive;

    private Pattern combinedPattern;

    private final String delimiter;

    final Map<String, List<Token>> expTokens = new HashMap<>();

    private ParserException lastException;
    private String lastExpression;

    private final Map<String, BigDecimal> constants = new HashMap<>();
    private final Map<String, Value> variables = new TreeMap<>();


    public Parser() {
        sensitive = false;
        delimiter = DEFAULT_SPLIT_CHARACTER;
        clearConstants();
    }

    public void addConstant(String name, BigDecimal value) {
        if (name != null) {
            constants.put(sensitive ? name : name.toUpperCase(), value);
            invalidatePattern();
        }
    }

    public void clearConstants() {
        constants.clear();
        addConstant("null", null);
        addConstant("pi", BigDecimal.valueOf(Math.PI));
        invalidatePattern();
    }

    public BigDecimal getConstant(String name) {
        return name == null ? null : constants.get(sensitive ? name : name.toUpperCase());
    }

    public String getConstantRegex() {
        List<String> names = new ArrayList<>(constants.keySet());
        names.sort(Collections.<String>reverseOrder());

        StringBuilder sb = new StringBuilder();
        for (String name : names) {
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
            for (TokenType type : TokenType.values()) {
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

    public Map<String, Value> getVars() {
        return variables;
    }

    private boolean isType(Token token, int type) throws ParserException {
        if (!token.isOperator()) {
            throw new ParserException("error.expected_operator_token",
                    token.getRow(),
                    token.getColumn()
            );
        }

        Operator o = Operator.find(token);
        if (o == null) {
            throw new ParserException(
                    "error.operator_not_found",
                    token.getRow(),
                    token.getColumn()
            );
        }

        return o.getAssociation() == type;
    }

    private boolean shouldPopToken(Token token, Token topOfStack, boolean caseSensitive) throws ParserException {
        // Unary minus/plus are handled differently if the top token is the exponentiation operator
        Operator op = Operator.find(token);
        if (op.inSet(Operator.UNARY_MINUS, Operator.UNARY_PLUS)) {
            return false;
        } else {
            return
                    (this.isType(token, Operator.LEFT_ASSOCIATIVE) && this.compareTokens(token, topOfStack) >= 0) ||
                            (this.isType(token, Operator.RIGHT_ASSOCIATIVE) && compareTokens(token, topOfStack) > 0);
        }
    }

    private int compareTokens(Token token1, Token token2) throws ParserException {
        if (!token1.isOperator()) {
            throw new ParserException(
                    "error.operator_expected",
                    token1.getRow(),
                    token1.getColumn()
            );
        } else if (!token2.isOperator()) {
            throw new ParserException(
                    "error.operator_expected_at_top",
                    token1.getRow(),
                    token1.getColumn()
            );
        }

        Operator o1 =  Operator.find(token1);
        if (o1 == null) {
            throw new ParserException(
                    "error.operator_not_found",
                    token1.getRow(),
                    token1.getColumn()
            );
        }

        Operator o2 =  Operator.find(token2);
        if (o2 == null) {
            throw new ParserException(
                    "error.operator_not_found",
                    token2.getRow(),
                    token2.getColumn()
            );
        }

        return  o1.getPrecedence() - o2.getPrecedence();
    }

    public void clearCache() {
        expTokens.clear();
    }

    public ParserException getLastException() {
        return lastException;
    }

    public String getLastExpression() {
        return lastExpression;
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

    public Value evaluate(String source) throws ParserException {
        if (source == null) {
            source = "";
        }
        source += ";";
        lastException = null;

        Value value =  new Value("");

        String[] expressions = source.split(delimiter + SPLIT_REGEX);

        for (String expression : expressions) {
            if (expression.trim().length() > 0) {
                List<Token> tokens = expTokens.get(expression);
                if (tokens == null) {
                    lastExpression = expression;
                    tokens = new ArrayList<Token>();
                    List<Token> list = tokenize(expression, false);
                    if (list.size() > 0) {
                        tokens.addAll(generateRPN(list));
                        expTokens.put(expression, tokens);
                    }
                }

                // Evaluate the expression
                value = (tokens.size() > 0) ? computeRPN(tokens) : new Value("ERROR: EMPTY EXPRESSION");
            }
        }

        return value;
    }

    public List<Token> tokenize(String input, boolean wantWhitespace) throws ParserException {
        int offset = 0;
        int row = 1;

        List<Token> tokens = new ArrayList<>();

        Matcher matcher = getPattern(this).matcher(input);
        while (matcher.find()) {
            if (wantWhitespace || matcher.group(TokenType.WHITESPACE.name()) == null) {
                for (TokenType tokenType : TokenType.values()) {
                    if (matcher.group(tokenType.name()) != null) {
                        String text = tokenType.resolve(matcher.group(tokenType.name()));
                        Token token = new Token(tokenType, text, row, matcher.start() + 1 - offset);
                        token.saveOrgValue();
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
            for (Token token : tokens) {
                if (TokenType.NOMATCH.equals(token.getType())) {
                    throw new ParserException(
                            "error.invalid_token",
                            token.getRow(),
                            token.getColumn()
                    );
                }
            }
        }

        return tokens;
    }

    protected List<Token> generateRPN(List<Token> inputTokens) throws ParserException {
        List<Token> outputTokens = new ArrayList<>();

        Token lastToken = null;
        Stack<Token> stack = new Stack<>();

        int count = 0;
        for (Token token : inputTokens) {

            if (token.opEquals(Operator.L_PARENTHESIS)) {
                count++;
            } else if (token.opEquals(Operator.R_PARENTHESIS)) {
                count--;
                if (count < 0) {
                    throw new ParserException(
                            "error.missing_parenthesis",
                            token.getRow(),
                            token.getColumn()
                    );
                }
            }
        }

        if (count != 0) {
            Token token = inputTokens.get(inputTokens.size() - 1);
            throw new ParserException(
                    "error.missing_parenthesis",
                    token.getRow(),
                    token.getColumn()
            );
        }

        for (Token token : inputTokens) {
            if ((token.opEquals(Operator.MINUS) || token.opEquals(Operator.PLUS))) {
                boolean isUnary = lastToken == null || lastToken.isOperator() || lastToken.isParen();
                if (isUnary) {
                    if (token.opEquals(Operator.MINUS)) {
                        token.setText(Operator.UNARY_MINUS.getText());  // unary minus, parsers 1 - -1.0
                    } else {
                        token.setText(Operator.UNARY_PLUS.getText()); // unary plus, parses  1 + +1.0
                    }
                }
            }

            lastToken = token;

            if (!token.isOperator() &&
                (token.isNumber() || token.isConstant() || token.isIdentifier())) {
                outputTokens.add(token);
            } else if (token.opEquals(Operator.L_PARENTHESIS)) {
                stack.push(token);
            } else if (token.opEquals(Operator.R_PARENTHESIS)) {
                while (!stack.empty() && !stack.peek().opEquals(Operator.L_PARENTHESIS)) {
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

        // Copy the rest of the stack to the output
        while (!stack.empty()) {
            outputTokens.add(stack.pop());
        }

        return outputTokens;
    }

    private void assertBothNumbers(Token lhs, Token rhs) throws ParserException {
        if (lhs.getValue().getType() != ValueType.NUMBER || rhs.getValue().getType() != ValueType.NUMBER ) {
            throw new ParserException("error.both_must_be_numeric", lhs.getRow(), lhs.getColumn());

        }
    }

    private void assertSufficientStack(Token token, Stack<Token> stack) throws ParserException {
        if (stack.size() < 2) {
            throw new ParserException("error.syntax", token.getRow(), token.getColumn());
        }
    }

    private Token processOperators(Token token, Stack<Token> stack) throws ParserException {
        Token result = null;

        Operator op = Operator.find(token);

        assertSufficientStack(token, stack);
        Token rhs = stack.pop();
        Token lhs = stack.pop();

        try {
            if (op.equals(Operator.PLUS)) {
                // Addition
                assertBothNumbers(lhs, rhs);
                BigDecimal bd = lhs.asNumber().add(rhs.asNumber());
                bd = bd.setScale(getPrecision(), BigDecimal.ROUND_HALF_UP).stripTrailingZeros();
                result = new Token(TokenType.NUMBER, bd.toPlainString(), token.getRow(), token.getColumn());
            } else if (op.equals(Operator.MINUS)) {
                // Subtraction
                assertBothNumbers(lhs, rhs);
                BigDecimal bd = lhs.asNumber().subtract(rhs.asNumber());
                bd = bd.setScale(getPrecision(), BigDecimal.ROUND_HALF_UP).stripTrailingZeros();
                result = new Token(TokenType.NUMBER, bd.toPlainString(), token.getRow(), token.getColumn());
            } else if (op.equals(Operator.MULT)) {
                // Multiplication
                assertBothNumbers(lhs, rhs);
                BigDecimal bd = lhs.asNumber().multiply(rhs.asNumber());
                bd = bd.setScale(getPrecision(), BigDecimal.ROUND_HALF_UP).stripTrailingZeros();
                result = new Token(TokenType.NUMBER, bd.toPlainString(), token.getRow(), token.getColumn());
            } else if (op.equals(Operator.DIV)) {
                // Division
                assertBothNumbers(lhs, rhs);
                int divisorScale = rhs.asNumber().scale();
                int scale = lhs.asNumber().equals(BigDecimal.ZERO) ? divisorScale : getPrecision();
                BigDecimal bd = lhs.asNumber().divide(rhs.asNumber(), scale, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();
                result = new Token(TokenType.NUMBER, bd.toPlainString(), token.getRow(), token.getColumn());
            } else if (op.equals(Operator.IDIV)) {
                // Integer division
                assertBothNumbers(lhs, rhs);
                BigDecimal bd = lhs.asNumber().divideToIntegralValue(rhs.asNumber());
                result = new Token(TokenType.NUMBER, bd.toPlainString(), token.getRow(), token.getColumn());
            } else if (op.equals(Operator.MODULUS)) {
                // Modulus
                assertBothNumbers(lhs, rhs);
                BigDecimal bd = lhs.asNumber().remainder(rhs.asNumber());
                result = new Token(TokenType.NUMBER, bd.toPlainString(), token.getRow(), token.getColumn());
            }else if (op.equals(Operator.ASSIGNMENT)) {
                // Assignment
                if (lhs.isIdentifier()) {
                    // Trying to assign an uninitialized variable -- could also get here
                    if (rhs.getValue().getType().equals(ValueType.UNDEFINED)) {
                        throw new ParserException(
                                "error.expected_initialized",
                                rhs.getRow(),
                                rhs.getColumn()
                        );
                    }

                } else {
                    throw new ParserException(
                            "error.expected_identified",
                            lhs.getRow(),
                            lhs.getColumn()
                    );
                }
                Value value = variables.get(sensitive ? lhs.getText() : lhs.getText().toUpperCase());
                value.set(rhs.getValue());
            }
        } catch (ArithmeticException ex) {
            throw new ParserException(ex.getMessage(), ex, token.getRow(), token.getColumn());
        }

        return result;
    }

    public Value computeRPN(List<Token> tokens) throws ParserException {

        Stack<Token> stack = new Stack<>();
        for (Token token : tokens) {
             if (token.isConstant()) {
                BigDecimal bd = getConstant(token.getText());
                token.getValue().setValue(bd);
                stack.push(token);
            } else if (token.isIdentifier()) {
                // Retrieve the value referenced by the identifier or create an empty placeholder
                Value value = variables.get(sensitive ? token.getText() : token.getText().toUpperCase());

                if (value == null) {
                    value = new Value();
                    value.set(token.getValue());
                    variables.put(sensitive ? token.getText() : token.getText().toUpperCase(), value);
                }
                token.getValue().set(value);
                stack.push(token);
            } else if (token.isOperator()) {
                // Handle unary minus (negation) and plus
                Operator op = Operator.find(token);
                if (Operator.UNARY_MINUS.equals(op)) {
                    Value value = stack.pop().getValue();
                    if (value.getType() == ValueType.NUMBER) {
                        value.setValue(value.asNumber().negate());
                        stack.push(new Token(TokenType.NUMBER, value.toString(), token.getRow(), token.getColumn()));
                    } else {
                        throw new ParserException(
                                "error.type_mismatch",
                                token.getRow(),
                                token.getColumn()
                        );
                    }
                    continue;
                } else if (op.equals(Operator.UNARY_PLUS)) {
                    continue;
                }

                // If an assignment has occurred, the result should not be pushed on the stack
                Token result = processOperators(token, stack);
                if (result != null) {
                    stack.push(result);
                }
            } else {
                stack.push(token);
            }
        }

        // Stack should have been consumed except for the final result
        if (stack.size() > 1) {
            throw new ParserException(
                    "error.syntax",
                    stack.get(0).getRow(),
                    stack.get(0).getColumn()
            );
        }

        return stack.size() == 0 ? new Value("empty result") : stack.pop().getValue();

    }

}
