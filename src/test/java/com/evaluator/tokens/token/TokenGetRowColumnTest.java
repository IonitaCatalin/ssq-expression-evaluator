package com.evaluator.tokens.token;

import com.evaluator.tokens.Token;
import com.evaluator.tokens.TokenType;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TokenGetRowColumnTest {

    @Test
    public void testGetRow() throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {
        Token token = new Token(TokenType.NUMBER, "20", 200 , 201);
        assertEquals(token.getRow(), 200);
    }
}
