package com.evaluator.tokens;

import com.evaluator.operators.Operator;
import com.evaluator.parser.Parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum TokenType {
    NUMBER("(?:\\b[0-9]+(?:\\.[0-9]*)?|\\.[0-9]+\\b)(?:[eE][-+]?[0-9]+\\b)?"),
    OPERATOR("~~dynamically-generated~~"),
    IDENTIFIER("[_A-Za-z][_A-Za-z0-9]*"),
    NEWLINE("\n"),
    EOS(";"),
    WHITESPACE("[ \\t]+"),
    NOMATCH(""),
    VALUE("");

    private final String regex;

    TokenType(String regex) {
        this.regex = regex;
    }

    public String getRegex(Parser parser) {
        if (this.equals(OPERATOR)) {
            return Operator.getOperatorRegex();
        } else {
            return regex;
        }
    }

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
