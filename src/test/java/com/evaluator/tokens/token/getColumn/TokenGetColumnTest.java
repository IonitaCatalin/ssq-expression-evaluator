package com.evaluator.tokens.token.getColumn;

import com.evaluator.tokens.Token;
import com.evaluator.tokens.TokenType;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.Assert.assertEquals;

public class TokenGetColumnTest {
    @Test
    public void testGetColumn() throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {
        Token token = new Token(TokenType.NUMBER, "20", 200 , 201);
        assertEquals(token.getColumn(), 201);
    }
}
