package com.evaluator.values.value;

import com.evaluator.types.BigInt;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;
import com.evaluator.values.Value;
import com.evaluator.values.ValueType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ValueTest {

    @Test
    public void testConstructor() {
        Value value = new Value();

        assertEquals(value.getType(), ValueType.UNDEFINED);
        assertNull(value.getName());
    }

    @Test
    public void testSecondConstructor() {
        Value value = new Value("valueAsString");
        assertEquals(value.asString(), "valueAsString");
    }

    @Test
    public void testThirdConstructor() throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {

        BigInt integer = new BigInt("2");
        Value value = new Value(integer);

        assertEquals(value.asNumber(), integer);
    }
}
