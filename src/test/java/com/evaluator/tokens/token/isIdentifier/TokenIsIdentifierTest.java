package com.evaluator.tokens.token.isIdentifier;

import com.evaluator.tokens.Token;
import com.evaluator.tokens.TokenType;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenIsIdentifierTest {

    @Test
    public void testIsIdentifierForNonIdentifierSymbol() throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {
        Token token = new Token(TokenType.OPERATOR, "-", 0, 0);
        assertFalse(token.isIdentifier());
    }

    @Test
    public void testIsIdentifierForIdentifierSymbol() throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {
        Token token = new Token(TokenType.IDENTIFIER, "a", 0, 0);
        assertTrue(token.isIdentifier());
    }

}
