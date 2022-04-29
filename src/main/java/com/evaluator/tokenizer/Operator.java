package com.evaluator.tokenizer;

import java.util.*;

public enum Operator {
    L_PARENTHESIS     (1, Operator.LEFT_ASSOCIATIVE, "(", "\\("),
    R_PARENTHESIS       (1, Operator.LEFT_ASSOCIATIVE, ")", "\\)"),

    UNARY_MINUS  (2, Operator.RIGHT_ASSOCIATIVE, "!", "!"),
    UNARY_PLUS   (2, Operator.RIGHT_ASSOCIATIVE, "!!", "!!"),

    MULT         (3, Operator.LEFT_ASSOCIATIVE, "*", "\\*"),
    DIV          (3, Operator.LEFT_ASSOCIATIVE, "/", "/"),
    IDIV         (3, Operator.LEFT_ASSOCIATIVE, "DIV", "div"),
    MODULUS      (3, Operator.LEFT_ASSOCIATIVE, "MOD", "mod"),
    PLUS         (4, Operator.LEFT_ASSOCIATIVE, "+", "\\+"),
    MINUS        (4, Operator.LEFT_ASSOCIATIVE, "-", "-"),

    ASSIGNMENT   (5, Operator.RIGHT_ASSOCIATIVE, "=", "=");

    public static final int NONE_ASSOCIATIVE  = 0;
    public static final int LEFT_ASSOCIATIVE  = 1;
    public static final int RIGHT_ASSOCIATIVE = 2;

    private static final Map<String, Operator> caseInsensitiveMap = new HashMap<String, Operator>();
    private static final Map<String, Operator> caseSensitiveMap = new HashMap<String, Operator>();

    static {
        for (Operator operator : values()) {
            caseInsensitiveMap.put(operator.text.toUpperCase(), operator);
            caseSensitiveMap.put(operator.text, operator);
        }
    }

    private final int precedence;
    private final int association;
    private final String text;
    private final String regex;

    Operator(int precedence, int association, String text, String regex) {
        this.precedence = precedence;
        this.association = association;
        this.text = text;
        this.regex = regex;
    }

    public static Operator find(Token token) {
        String key = (token == null || token.getText() == null) ? "" : token.getText().toUpperCase();
        return caseInsensitiveMap.get(key);
    }

    public boolean inSet(Operator... operators) {
        for (Operator operator : operators) {
            if (this.equals(operator)) {
                return true;
            }
        }
        return false;
    }

    public int getAssociation() {
        return association;
    }

    public int getPrecedence() {
        return precedence;
    }

    public String getText() {
        return text;
    }

    public static String getOperatorRegex() {
        List<String> regexs = new ArrayList<>();
        for (Operator operator : values()) {
            regexs.add(operator.regex);
        }

        Collections.sort(regexs, Collections.<String>reverseOrder());

        StringBuilder sb = new StringBuilder();
        for (String regex : regexs) {
            if (sb.length() > 0) {
                sb.append("|");
            }
            sb.append(regex);
        }

        return sb.toString();
    }

}
