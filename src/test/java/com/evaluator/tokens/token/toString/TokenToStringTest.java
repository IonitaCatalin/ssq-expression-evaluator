package com.evaluator.tokens.token.toString;

import com.evaluator.tokens.Token;
import com.evaluator.tokens.TokenType;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TokenToStringTest {

    @Test
    public void testToStringForNumber() throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {
        Token token = new Token(TokenType.NUMBER, "20", 1, 1);
        assertEquals(token.toString(),"type = " +
                TokenType.NUMBER.toString()+
                " value = " + 20 +
                " token = " + 20);
    }

    @Test
    public void testToStringForOperator() throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {
        Token token = new Token(TokenType.OPERATOR, "+", 1, 1);
        assertEquals(token.toString(),"type = " +
                TokenType.OPERATOR +
                " value = " + null +
                " token = " + "+");
    }

    @Test
    public void testToStringForIdentifier() throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {
        Token token = new Token(TokenType.IDENTIFIER, "a", 1, 1);
        assertEquals(token.toString(),"type = " +
                TokenType.IDENTIFIER +
                " value = " + null +
                " token = " + "a");
    }

}
