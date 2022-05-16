package com.evaluator.tokens.token.getValue;

import com.evaluator.tokens.Token;
import com.evaluator.tokens.TokenType;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class TokenGetValueTest {

    @Test
    public void testGetValue() throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {
        Token token = new Token(TokenType.NUMBER, "20", 0 ,0);
        assertEquals(token.getValue().asNumber().convertToString(), "20");
    }
}
