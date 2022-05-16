package com.evaluator.tokens.token;

import com.evaluator.tokens.Token;
import com.evaluator.tokens.TokenType;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {
    @Test
    public void testEmptyConstructor() {
        Token token = new Token();

        assertEquals(token.getRow(), 0);
        assertEquals(token.getColumn(), 0);
        assertNotNull(token.getValue());
        assertEquals(token.getText(), "");
    }

    @Test
    public void testNonEmptyConstructor() throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {
        Token token = new Token(TokenType.NUMBER,"20",0,0);
        assertEquals(token.getType(), TokenType.NUMBER);
        assertEquals(token.getRow(), 0);
        assertEquals(token.getColumn(), 0);
        assertEquals(token.getText(), "20");
        assertEquals(token.getValue().asNumber().convertToString(), "20");
    }
}
