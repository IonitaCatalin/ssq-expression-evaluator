package com.evaluator.types.bigint;

import com.evaluator.types.BigInt;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;
import com.evaluator.types.exceptions.NegativeValueException;
import org.junit.Assert;
import org.junit.Test;


public class SqrtTest {


    @Test
    public void sqrtTwoNumbersCorrectly() {

        try {
            BigInt b1 = new BigInt("1000000000");
            BigInt b2 = new BigInt("3");
            int i = 3;


            // simple add
            Assert.assertEquals(b1.sqrt(b2).convertToString(), "1000");
            Assert.assertEquals(b1.sqrt(i).convertToString(), "1000");

        } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException | NegativeValueException e) {
            Assert.fail();
        }
    }

    @Test
    public void sqrtWithNullElement() {

        try {
            BigInt b1 = new BigInt("123456");
            BigInt b2 = new BigInt("1");
            int i = 1;


            Assert.assertEquals(b1.sqrt(b2).convertToString(), b1.convertToString());
            Assert.assertEquals(b1.sqrt(i).convertToString(), b1.convertToString());


        } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException | NegativeValueException e) {
            Assert.fail();
        }
    }

    @Test
    public void powWithZero(){
        try {
            BigInt b1 = new BigInt("123456");
            BigInt b2 = new BigInt("0");
            int i = 0;


            Assert.assertEquals("1", b1.pow(b2).convertToString());
            Assert.assertEquals("1", b1.pow(i).convertToString());

        } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException | NegativeValueException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

}
