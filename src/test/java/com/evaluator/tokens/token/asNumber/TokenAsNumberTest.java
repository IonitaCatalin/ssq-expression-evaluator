package com.evaluator.tokens.token.asNumber;

import com.evaluator.tokens.Token;

import com.evaluator.types.BigInt;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;
import com.evaluator.values.Value;

import org.junit.Test;

import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class TokenAsNumberTest {


    @Test
    @DisplayName("token.asNumber method with value as non-null value ")
    public void testAsNumberWithNonNullValue() throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {

        Token token = mock(Token.class);

        Value mockedValue = mock(Value.class);
        BigInt integer = new BigInt("2");

        when(token.asNumber()).thenCallRealMethod();
        when(token.getValue()).thenReturn(mockedValue);
        when(mockedValue.asNumber()).thenReturn(integer);

        assertEquals(token.asNumber(), integer);

    }

    @Test
    public void testAsNumberWithNullValue() throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {
        Token token = mock(Token.class);

        Value mockedValue = mock(Value.class);

        when(token.asNumber()).thenCallRealMethod();
        when(token.getValue()).thenReturn(mockedValue);
        when(mockedValue.asNumber()).thenReturn(null);

        assertEquals(token.asNumber(), null);
    }
}
