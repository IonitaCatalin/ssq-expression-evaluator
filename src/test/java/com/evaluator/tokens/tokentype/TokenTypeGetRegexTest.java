package com.evaluator.tokens.tokentype;

import com.evaluator.parser.Parser;
import com.evaluator.tokens.TokenType;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TokenTypeGetRegexTest {

    @Test
    public void testGetRegexForOperator() {
        assertEquals(TokenType.OPERATOR.getRegex(new Parser()),
                "\\^|\\+|\\*|\\)|\\(|\\#|=|/|-|,");
    }

    @Test
    public void testGetRegexForNonOperator() {
        assertEquals(TokenType.NUMBER.getRegex(new Parser()),
                "(?:\\b[0-9]+(?:\\.[0-9]*)?|\\.[0-9]+\\b)(?:[eE][-+]?[0-9]+\\b)?");
    }
}
