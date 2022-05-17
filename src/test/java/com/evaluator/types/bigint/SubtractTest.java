package com.evaluator.types.bigint;


import com.evaluator.types.BigInt;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;
import com.evaluator.types.exceptions.NegativeValueException;
import org.junit.Assert;
import org.junit.Test;
import util.TestUtil;


public class SubtractTest {


    @Test
    public void subtractTwoNumbersCorrectly() {

        try {
            BigInt b1 = new BigInt("123456");
            BigInt b2 = new BigInt("654321");

            Assert.assertEquals(b2.subtract(b1).convertToString(), "530865");
        } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException | NegativeValueException e) {
            Assert.fail();
        }
    }

    @Test
    public void subtractWithNullElement(){
        try {
            BigInt b1 = new BigInt("123456");
            BigInt b2 = new BigInt("0");

            // null element
            Assert.assertEquals(b1.subtract(b2).convertToString(), b1.convertToString());

        } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException | NegativeValueException e) {
            Assert.fail();
        }
    }

    @Test
    public void subtractWithRemainingZerosInFront() {

        try {
            BigInt b1 = new BigInt("123456");
            BigInt b2 = new BigInt("123455");

            Assert.assertEquals(b1.subtract(b2).convertToString(), "1");
        } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException | NegativeValueException e) {
            Assert.fail();
        }
    }

    @Test
    public void subtractWithCarry(){
        try {
            BigInt b1 = new BigInt("123456");
            BigInt b2 = new BigInt("111111111110000000000");

            Assert.assertEquals(b2.subtract(b1).convertToString(), "111111111109999876544");
        } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException | NegativeValueException e) {
            Assert.fail();
        }
    }

    @Test
    public void subtractTwoNumbersAndThrowErrorBecauseResultIsNegative() throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {

        try {
            BigInt b1 = new BigInt("123456");
            BigInt b2 = new BigInt("654321");

            b1.subtract(b2);

            //Should not get here
            Assert.fail();
        } catch (NegativeValueException e) {
            Assert.assertTrue(true);
        }
    }

}