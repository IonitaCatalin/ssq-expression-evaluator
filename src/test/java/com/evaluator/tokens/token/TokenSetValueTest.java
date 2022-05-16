package com.evaluator.tokens.token;

import com.evaluator.tokens.Token;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;
import com.evaluator.values.Value;
import org.junit.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TokenSetValueTest {
    @Test
    public void setValueTest() throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {

        Value value = new Value();
        Token token = new Token();

        token.setValue(value);
        assertTrue(new ReflectionEquals(token.getValue()).matches(value));

    }
}
