package com.evaluator.tokens.tokentype.unescapeString;

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

}
