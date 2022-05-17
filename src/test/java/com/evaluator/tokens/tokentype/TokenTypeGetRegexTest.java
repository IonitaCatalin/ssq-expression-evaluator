package com.evaluator.tokens.tokentype;

import com.evaluator.operators.Operator;
import com.evaluator.parser.Parser;
import com.evaluator.tokens.TokenType;

import org.mockito.MockedStatic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mockStatic;

public class TokenTypeGetRegexTest {


    @Test
    public void testGetRegexForOperator() {
        MockedStatic<Operator> mockedOperator = mockStatic(Operator.class);

        mockedOperator.when(Operator::getOperatorRegex).thenReturn("~some-regex~");
        assertEquals(TokenType.OPERATOR.getRegex(new Parser()),
                "~some-regex~");

        mockedOperator.close();
    }

    @Test
    public void testGetRegexForNonOperator() {
        assertEquals(TokenType.NUMBER.getRegex(new Parser()),
                "(?:\\b[0-9]+(?:\\.[0-9]*)?|\\.[0-9]+\\b)(?:[eE][-+]?[0-9]+\\b)?");
    }
}
