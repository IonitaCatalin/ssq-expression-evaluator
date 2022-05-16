package com.evaluator.types.bigint;


import com.evaluator.types.BigInt;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;
import org.junit.Assert;
import org.junit.Test;

public class ConvertToStringTest {

    @Test
    public void convertToString() {
        try {
            BigInt b1 = new BigInt("123456");
            BigInt b2 = new BigInt();

            Assert.assertEquals("123456", b1.convertToString());
            Assert.assertEquals("", b2.convertToString());
        } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException e) {
            Assert.fail();
        }

    }

    @Test
    public void convertToStringF() {
        try {
            BigInt b1 = new BigInt("123456");
            BigInt b2 = new BigInt();

            Assert.assertEquals("123456", b1.toString());
            Assert.assertEquals("", b2.toString());
        } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException e) {
            Assert.fail();
        }

    }
}