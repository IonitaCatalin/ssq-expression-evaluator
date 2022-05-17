package com.evaluator.values.value;

import com.evaluator.values.Value;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ValueAsStringTest {

    @Test
    public void testAsString() {
        Value value = new Value("test-string");
        assertEquals(value.asString(), "test-string");
    }
}
