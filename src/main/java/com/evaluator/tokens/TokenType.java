package com.evaluator.tokens;

import com.evaluator.operators.Operator;

/**
 * TokenType enum, represents all the acceptable types of tokens
 *
 * @author Ionita Mihail-Catalin
 * @since 01.05.2022
 */

public enum TokenType {
    /**
     * The regex pattern for number token
     */
    NUMBER("(?:\\b[0-9]+(?:\\.[0-9]*)?|\\.[0-9]+\\b)(?:[eE][-+]?[0-9]+\\b)?"),

    /**
     * The regex pattern for operator token
     */
    OPERATOR("~~dynamically-generated~~"),

    /**
     * The regex pattern for identifier token
     */
    IDENTIFIER("[_A-Za-z][_A-Za-z0-9]*"),

    /**
     * The regex pattern for newline token
     */
    NEWLINE("\n"),

    /**
     * The regex pattern for end-of-stream token
     */
    EOS(";"),

    /**
     * The regex pattern for whitespaces
     */
    WHITESPACE("[ \\t]+"),

    /**
     * The regex pattern for nomatch operator
     */
    NOMATCH(""),

    /**
     * The regex pattern for value operator
     */
    VALUE("");

    /**
     * The regex for a type of token
     */
    private final String regex;

    TokenType(String regex) {
        this.regex = regex;
    }

    /**
     * Gets the regex for a TokenType enum
     */
    public String getRegex() {
        if (this.equals(OPERATOR)) {
            return Operator.getOperatorRegex();
        } else {
            return regex;
        }
    }

    /**
     * Unescapes the escaped character from a string, including the UTF-8 characters
     *
     * @param str The string
     * @return The unescaped string
     */
    public static String unescapeString(String str) {
        if (str == null || str.trim().length() == 0) {
            return str;
        }

        StringBuilder sb = new StringBuilder(str.length());

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (ch == '\\') {
                char nextChar = (i == str.length() - 1) ? '\\' : str.charAt(i + 1);

                if (nextChar >= '0' && nextChar <= '7') {
                    String code = "" + nextChar;
                    i++;
                    if ((i < str.length() - 1) && str.charAt(i + 1) >= '0' && str.charAt(i + 1) <= '7') {
                        code += str.charAt(i + 1);
                        i++;
                        if ((i < str.length() - 1) && str.charAt(i + 1) >= '0' && str.charAt(i + 1) <= '7') {
                            code += str.charAt(i + 1);
                            i++;
                        }
                    }
                    sb.append(Character.toChars(Integer.parseInt(code, 8)));
                    continue;
                }

                switch (nextChar) {
                    case '\"':
                        ch = '\"';
                        break;
                    case '\'':
                        ch = '\'';
                        break;
                    case '\\':
                        ch = '\\';
                        break;
                    case 'f':
                        ch = '\f';
                        break;
                    case 'b':
                        ch = '\b';
                        break;
                    case 'n':
                        ch = '\n';
                        break;
                    case 'r':
                        ch = '\r';
                        break;
                    case 't':
                        ch = '\t';
                        break;
                    case 'u':
                        if (i < str.length() - 5) {
                            try {
                                String code = "" + str.charAt(i + 2) + str.charAt(i + 3)
                                        + str.charAt(i + 4) + str.charAt(i + 5);
                                sb.append(Character.toChars(Integer.parseInt(code, 16)));
                            } catch (NumberFormatException ex) {
                                // Ignore poorly formed Unicode escape sequence
                            }
                            i += 5;
                            continue;
                        } else {
                            ch = 'u';
                            break;
                        }
                    default:
                        i--;
                        break;
                }
                i++;
            }
            sb.append(ch);
        }
        return sb.toString();
    }

    public String resolve(String text) {
        return text;
    }

}
