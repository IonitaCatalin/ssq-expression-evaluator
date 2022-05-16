package com.evaluator.types.bigint;

import com.evaluator.types.BigInt;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;
import org.junit.Assert;
import org.junit.Test;

public class EqualsTest {

    @Test
    public void numbersShouldBeEqual(){
        try {
            BigInt b1 = new BigInt("123456789");
            BigInt b2 = new BigInt("123456789");

            Assert.assertEquals(b1, b2);
            Assert.assertEquals(b1, b1);

        } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException e) {
            Assert.fail();
        }
    }

    @Test
    public void numbersShouldNotBeEqualIfTheyDontHaveTheSameValue(){
        try {
            BigInt b1 = new BigInt("123456789");
            BigInt b2 = new BigInt("123456788");

            Assert.assertNotEquals(b1, b2);
            Assert.assertNotEquals(b2, b1);

        } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException e) {
            Assert.fail();
        }
    }

    @Test
    public void numbersShouldNotBeEqualIfTheyDontHaveTheSameNumberOfDigits(){
        try {
            BigInt b1 = new BigInt("123456789");
            BigInt b2 = new BigInt("12345678");

            Assert.assertNotEquals(b1, b2);
            Assert.assertNotEquals(b2, b1);

        } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException e) {
            Assert.fail();
        }
    }
}
