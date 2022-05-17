package com.evaluator.types.bigint;


import com.evaluator.types.BigInt;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;
import org.junit.Assert;
import org.junit.Test;

public class CompareToTest {

    @Test
    public void numbersShouldBeEqual(){
        try {
            BigInt b1 = new BigInt("123456789");
            BigInt b2 = new BigInt("123456789");

            Assert.assertEquals(0, b1.compareTo(b2));
            Assert.assertEquals(0, b1.compareTo(b1));
        } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException e) {
            Assert.fail();
        }

    }

    @Test
    public void numberShouldBeBigger(){
        try {
            BigInt b1 = new BigInt("123456789");
            BigInt b2 = new BigInt("123456788");
            BigInt b3 = new BigInt("12345678");

            Assert.assertEquals(1, b1.compareTo(b2));
            Assert.assertEquals(1, b1.compareTo(b3));

        } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException e) {
            Assert.fail();
        }
    }

    @Test
    public void numberShouldBeSmaller(){
        try {
            BigInt b1 = new BigInt("123456788");
            BigInt b2 = new BigInt("123456789");
            BigInt b3 = new BigInt("1234567890");

            Assert.assertEquals(-1, b1.compareTo(b2));
            Assert.assertEquals(-1, b1.compareTo(b3));

        } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException e) {
            Assert.fail();
        }
    }

}
