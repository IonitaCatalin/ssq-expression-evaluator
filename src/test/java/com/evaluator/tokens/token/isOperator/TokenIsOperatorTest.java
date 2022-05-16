package com.evaluator.tokens.token.isOperator;

import com.evaluator.tokens.Token;
import com.evaluator.tokens.TokenType;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TokenIsOperatorTest {
    @Test
    public void testIsOperatorForNonOperatorSymbol() throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {
        Token token = new Token(TokenType.IDENTIFIER, "a", 0, 0);
        assertFalse(token.isOperator());
    }

    @Test
    public void testIsOperatorForOperatorSymbol() throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {
        Token token = new Token(TokenType.OPERATOR, "=", 0, 0);
        assertTrue(token.isOperator());
    }
}
