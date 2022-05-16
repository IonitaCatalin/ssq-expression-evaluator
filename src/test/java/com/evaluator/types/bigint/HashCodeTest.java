package com.evaluator.types.bigint;

import com.evaluator.types.BigInt;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;

public class HashCodeTest {
    @Test
    public void shouldReturnHashCode(){
        try {
            BigInt b = new BigInt("123456789");
            Assert.assertEquals(-427046660, b.hashCode());
        } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException e) {
            e.printStackTrace();
        }

    }
}
