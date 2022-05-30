package com.evaluator.operators;

import com.evaluator.tokens.Token;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The operator class, used for representing a matchable operator in an expression
 *
 * @author Ionita Mihail-Catalin
 * @author Popa Stefan
 * @since 01.05.2022
 */
public enum Operator {
    /**
     *  The Operator value for a comma
     */
    COMMA               (1, Operator.NONE_ASSOCIATIVE, ",", ","),

    /**
     *  The Operator value for a left parenthesis
     */
    L_PARENTHESIS       (1, Operator.LEFT_ASSOCIATIVE, "(", "\\("),

    /**
     *  The Operator value for a right paranthesis
     */
    R_PARENTHESIS       (1, Operator.LEFT_ASSOCIATIVE, ")", "\\)"),

    /**
     *  The Operator value for the pow operator
     */
    POW          (2, Operator.LEFT_ASSOCIATIVE,"^", "\\^"),

    /**
     *  The Operator value for the sqrt operator
     */
    SQRT         (2, Operator.LEFT_ASSOCIATIVE,"#","\\#"),

    /**
     *  The Operator value for the mult operator
     */
    MULT         (3, Operator.LEFT_ASSOCIATIVE, "*", "\\*"),

    /**
     *  The Operator value for the div operator
     */
    DIV          (3, Operator.LEFT_ASSOCIATIVE, "/", "/"),

    /**
     *  The Operator value for the plus operator
     */
    PLUS         (4, Operator.LEFT_ASSOCIATIVE, "+", "\\+"),

    /**
     *  The Operator value for the minus operator
     */
    MINUS        (4, Operator.LEFT_ASSOCIATIVE, "-", "-"),

    /**
     *  The Operator value for the assignment operator
     */
    ASSIGNMENT   (5, Operator.RIGHT_ASSOCIATIVE, "=", "=");

    /**
     *  Value for no associativity of operator
     */
    public static final int NONE_ASSOCIATIVE = 0;

    /**
     *  Value for left associativity of the operator
     */
    public static final int LEFT_ASSOCIATIVE  = 1;

    /**
     *  Value for right associativity of the operator
     */
    public static final int RIGHT_ASSOCIATIVE = 2;

    /**
     *  Map for the case-insensitive variables
     */
    private static final Map<String, Operator> caseInsensitiveMap = new HashMap<>();

    /**
     *  Map for the case-sensitive variables
     */
    private static final Map<String, Operator> caseSensitiveMap = new HashMap<>();

    static {
        for (Operator operator : values()) {
            caseInsensitiveMap.put(operator.text.toUpperCase(), operator);
            caseSensitiveMap.put(operator.text, operator);
        }
    }

    /**
     *  The precedence of the operator
     */
    private final int precedence;

    /**
     *  Map for the case-sensitive variables
     */
    private final int association;

    /**
     *  The text of the operator
     */
    private final String text;

    /**
     * The regex of the operator
     */
    private final String regex;

    Operator(int precedence, int association, String text, String regex) {
        this.precedence = precedence;
        this.association = association;
        this.text = text;
        this.regex = regex;
    }

    /**
     *  Finds the token
     *
     * @param  token The Token
     * @return The operator
     */
    public static Operator find(Token token) {
        String key = (token == null || token.getText() == null) ? "" : token.getText().toUpperCase();
        return caseInsensitiveMap.get(key);
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


    /**
     *  Gets the regex for an operator
     * @return The regex as a string
     */
    public static String getOperatorRegex() {
        List<String> regexs = new ArrayList<>();
        for (Operator operator : values()) {
            regexs.add(operator.regex);
        }

        regexs.sort(Collections.<String>reverseOrder());

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
