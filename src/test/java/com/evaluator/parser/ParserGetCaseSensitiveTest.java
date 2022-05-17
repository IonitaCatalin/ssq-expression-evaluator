package com.evaluator.parser;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class ParserGetCaseSensitiveTest {

    @Test
    public void testGetCaseSensitive() {
        Parser parser = new Parser();
        assertFalse(parser.getCaseSensitive());
    }
}
