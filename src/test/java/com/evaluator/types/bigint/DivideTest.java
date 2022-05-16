package com.evaluator.types.bigint;


import com.evaluator.types.BigInt;
import com.evaluator.types.exceptions.DivisionByZeroException;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;
import com.evaluator.types.exceptions.NegativeValueException;
import org.junit.Assert;
import org.junit.Test;


public class DivideTest {


    @Test
    public void divideTwoNumbersCorrectly() {

        try {
            BigInt b1 = new BigInt("123456");
            BigInt b2 = new BigInt("123456");
            BigInt b3 = new BigInt("6");
            int i1 = 123456;
            int i2 = 6;

            Assert.assertEquals("1", b2.divide(b1).convertToString());
            Assert.assertEquals("1", b2.divide(i1).convertToString());

            Assert.assertEquals("20576", b2.divide(b3).convertToString());
            Assert.assertEquals("20576", b2.divide(i2).convertToString());
        } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException | NegativeValueException | DivisionByZeroException e) {
            Assert.fail();
        }
    }

    @Test
    public void divideWithNullElement(){
        try {
            BigInt b1 = new BigInt("123456");
            BigInt b2 = new BigInt("1");
            int i1 = 1;

            // null element
            Assert.assertEquals(b1.divide(b2).convertToString(), b1.convertToString());
            Assert.assertEquals(b1.divide(i1).convertToString(), b1.convertToString());

        } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException | NegativeValueException | DivisionByZeroException e) {
            Assert.fail();
        }
    }


    @Test
    public void divideTwoNumbersAndThrowErrorBecauseDividerIsNegative() throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {

        try {
            BigInt b1 = new BigInt("123456");
            BigInt b2 = new BigInt("0");

            b1.divide(b2);

            //Should not get here
            Assert.fail();
        } catch (DivisionByZeroException | NegativeValueException e) {
            Assert.assertTrue(true);
        }

        try {
            BigInt b1 = new BigInt("123456");
            int i = 0;

            b1.divide(i);

            //Should not get here
            Assert.fail();
        } catch (DivisionByZeroException e) {
            Assert.assertTrue(true);
        }
    }

}