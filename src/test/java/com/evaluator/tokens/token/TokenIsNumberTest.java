package com.evaluator.tokens.token;

import com.evaluator.tokens.Token;
import com.evaluator.tokens.TokenType;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TokenIsNumberTest {

    @Test
    public void testIsNumberForNonNumberSymbol() throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {
        Token token = new Token(TokenType.IDENTIFIER, "a", 0, 0);
        assertFalse(token.isNumber());
    }

    @Test
    public void testIsNumberForNumberSymbol() throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {
        Token token = new Token(TokenType.NUMBER, "20", 0, 0);
        assertTrue(token.isNumber());
    }

}
