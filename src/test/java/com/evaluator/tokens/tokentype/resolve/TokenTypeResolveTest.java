package com.evaluator.tokens.tokentype.resolve;

import com.evaluator.tokens.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TokenTypeResolveTest {
    @Test
    public void testResolveForNullPattern() {
        assertNull(TokenType.NOMATCH.resolve(null));
    }

    @Test
    public void testResolveForNonNullPattern() {
        assertEquals(TokenType.NUMBER.resolve("12"), "12");
    }

    @Test
    public void testResolveForEscapedCharacters() {
        assertEquals(TokenType.NUMBER.resolve("\\a"), "\\a");
    }
}