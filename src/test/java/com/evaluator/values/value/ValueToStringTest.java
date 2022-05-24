package com.evaluator.values.value;

import com.evaluator.types.BigInt;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;
import com.evaluator.values.Value;
import com.evaluator.values.ValueType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ValueToStringTest {

    @Test
    public void testToString() throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {
        Value value = new Value();

        value.setValue(new BigInt("2"));
        value.setType(ValueType.NUMBER);
        value.setName("some-number");

        assertEquals(value.toString(), "type = NUMBER | value = 2 | text = null");
    }

    @Test
    public void testToStringForNonString() {
        Value value = new Value();

        value.setValue(new BigInt(0));
        value.setType(ValueType.UNDEFINED);
        value.setName("some-undefined");

        assertEquals(value.toString(), "type = UNDEFINED | value = 0 | text = null");
    }
}
