package com.evaluator.values.value;

import com.evaluator.values.Value;
import com.evaluator.values.ValueType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ValueClearTest {

    @Test
    public void testClear() {

        Value value = new Value();
        Value valueAfterClear = value.clear();
        assertEquals(valueAfterClear.getType(), ValueType.UNDEFINED);

    }
}
