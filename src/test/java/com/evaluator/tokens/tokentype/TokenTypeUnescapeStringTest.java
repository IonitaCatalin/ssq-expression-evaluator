package com.evaluator.tokens.tokentype;

import com.evaluator.tokens.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TokenTypeUnescapeStringTest {
    @Test
    public void testUnescapeStringForNullString () {
        assertNull(TokenType.unescapeString(null));
    }

    @Test
    public void testUnescapeStringForNonNullString () {
        assertEquals(TokenType.unescapeString("\\a"), "\\a");
    }

    @Test
    public void testUnescapeStringForUniCode() {
        assertEquals(TokenType.unescapeString("\\11234"), "J34");
    }

    @Test
    public void testUnescapeStringForSpecialCharacters(){
        assertEquals(TokenType.unescapeString("\\\""), "\"");
        assertEquals(TokenType.unescapeString("\\\\"), "\\");
        assertEquals(TokenType.unescapeString("\\\'"), "\'");
        assertEquals(TokenType.unescapeString("\\f"), "\f");
        assertEquals(TokenType.unescapeString("\\b"), "\b");
        assertEquals(TokenType.unescapeString("\\n"), "\n");
        assertEquals(TokenType.unescapeString("\\r"), "\r");
        assertEquals(TokenType.unescapeString("\\t"), "\t");
        assertEquals(TokenType.unescapeString("\\u"), "u");
        assertEquals(TokenType.unescapeString("\\u1234"), "\u1234");

    }
}
