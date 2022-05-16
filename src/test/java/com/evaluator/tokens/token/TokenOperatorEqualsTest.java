package com.evaluator.tokens.token;

import com.evaluator.operators.Operator;
import com.evaluator.tokens.Token;
import com.evaluator.tokens.TokenType;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class TokenOperatorEqualsTest {

    @Test
    public void testOperatorEqualsWithNullText() {
        Token token = new Token();

        token.setText(null);
        assertFalse(token.operatorEquals(Operator.ASSIGNMENT));

    }

    @Test
    public void testOperatorEqualsWithNullOperator() {
        Token token = new Token();
        assertFalse(token.operatorEquals((Operator) null));
    }

    @Test
    public void testOperatorEqualsFindsMatch() throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {
        Token token = new Token(TokenType.OPERATOR, "+", 1,1);
        assertTrue(token.operatorEquals(Operator.PLUS));
    }

}
