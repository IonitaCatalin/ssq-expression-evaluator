package com.evaluator.types.bigint.converttostring;


import com.evaluator.types.BigInt;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;
import org.junit.Assert;
import org.junit.Test;

public class BigIntConvertToStringTest {

    @Test
    public void convertToString() {
        try {
            BigInt b = new BigInt("123456");

            Assert.assertEquals("123456", b.convertToString());
        } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException e) {
            e.printStackTrace();
        }

    }
}