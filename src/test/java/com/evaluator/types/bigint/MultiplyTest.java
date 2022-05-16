package com.evaluator.types.bigint;


import com.evaluator.types.BigInt;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;
import org.junit.Assert;
import org.junit.Test;
import util.TestUtil;


public class MultiplyTest {


    @Test
    public void multiplyTwoNumbersCorrectly() {

        try {
            BigInt b1 = new BigInt("123456");
            BigInt b2 = new BigInt("654321");


            // simple add
            Assert.assertEquals(b1.multiply(b2).convertToString(), "80779853376");

        } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException e) {
            Assert.fail();
        }
    }

    @Test
    public void multiplyWithNullValue() {

        try {
            BigInt b1 = new BigInt("123456");
            BigInt b2 = new BigInt("1");


            Assert.assertEquals(b1.multiply(b2).convertToString(), b1.convertToString());

        } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException e) {
            Assert.fail();
        }
    }

    @Test
    public void multiplyWithZero(){
        try {
            BigInt b1 = new BigInt("123456");
            BigInt b2 = new BigInt("0");
            BigInt b3 = new BigInt();


            Assert.assertEquals("0", b1.multiply(b2).convertToString());
            Assert.assertEquals("0", b1.multiply(b3).convertToString());
            Assert.assertEquals("0", b3.multiply(b1).convertToString());

        } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException e) {
            Assert.fail();
        }
    }

    @Test
    public void multiplicationCommutativity(){
        try {
            BigInt b1 = new BigInt("123456");
            BigInt b2 = new BigInt("654321");
            BigInt b3 = new BigInt("111111");

            Assert.assertEquals(b1.multiply(b2).convertToString(), b2.multiply(b1).convertToString());
            Assert.assertEquals(b1.multiply(b2).multiply(b3).convertToString(), b3.multiply(b2).multiply(b1).convertToString());
        } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException e) {
            Assert.fail();
        }
    }

    @Test
    public void multiplyTwoNumbersAndThrowErrorBecauseNumberIsTooBig() throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {

        try {
            BigInt b1 = new BigInt(TestUtil.getRandomNumber(50_001));
            BigInt b2 = new BigInt(TestUtil.getRandomNumber(50_001));

            b1.multiply(b2);

            //Should not get here
            Assert.fail();
        } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException e) {
            Assert.assertTrue(true);
        }
    }

}