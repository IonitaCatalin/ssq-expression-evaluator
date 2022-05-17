package com.evaluator.values.value;

import com.evaluator.values.Value;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ValueGetNameTest {

    @Test
    public void testGetName() {
        Value value = new Value("test-string");
        value.setName("value-name");

        assertEquals(value.getName(), "value-name");
    }
}
