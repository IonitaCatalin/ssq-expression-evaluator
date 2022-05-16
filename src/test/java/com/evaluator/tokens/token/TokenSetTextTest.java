package com.evaluator.tokens.token;

import com.evaluator.tokens.Token;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TokenSetTextTest {
    @Test
    public void testSetText() {
        Token token = new Token();
        token.setText("text-test");
        assertEquals(token.getText(), "text-test");
    }
}
