package com.evaluator.utils;

import com.evaluator.tokens.Token;
import com.evaluator.tokens.TokenType;
import com.evaluator.values.ValueType;

import java.util.List;
import java.util.Stack;

/**
 * @description Helper class used for adding assertion with specific conditions
 *
 * @author Ionita Mihail-Catalin
 * @since 01.05.2022
 */
public class Conditions {

    public static boolean areOperandsNumeric(Token lhs, Token rhs) {
        return lhs.getValue().getType() == ValueType.NUMBER && rhs.getValue().getType() == ValueType.NUMBER;
    }

    public static boolean isStackSizeSufficient(Stack<Token> stack, int requiredSize)  {
        return stack.size() >= requiredSize;
    }

    public static boolean isTokenOfType(Token value, TokenType type) {
        return value.getType() == type;
    }

    public static boolean isNotNull(Object object) {
        return object != null;
    }

    public static boolean isNull(Object object) {
        return object == null;
    }

    public static boolean hasNElements(List<Object> list, int count) {
        return list.size() <= count;
    }

    public static boolean isOfSize(List<Object> list, int size) {
        return list.size() == size;
    }

    public static boolean isIdenticalList(List<Object> original, List<Object> current) {
        return original.equals(current);
    }

    public static boolean areEqual(Object f, Object s) {
        return f.equals(s);
    }

    
}
