package com.evaluator.tokens.tokentype;

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
        assertEquals(TokenType.IDENTIFIER.resolve("\\a\\b\\f"), "\\a\\b\\f");
    }

}
