package com.evaluator.types.bigint;


import com.evaluator.types.BigInt;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;
import org.junit.Assert;
import org.junit.Test;
import util.TestUtil;


public class AddTest {


    @Test
    public void addTwoNumbersCorrectly() {

        try {
            BigInt b1 = new BigInt("123456");
            BigInt b2 = new BigInt("654321");

            Assert.assertEquals(b1.add(b2).convertToString(), "777777");

        } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException e) {
            Assert.fail();
        }
    }

    @Test
    public void addWithTheAdditionNullElement(){
        try {
            BigInt b1 = new BigInt("123456");
            BigInt b2 = new BigInt("0");
            Assert.assertEquals(b1.add(b2).convertToString(), b1.convertToString());
        } catch (InvalidNumberFormatException | MaximumNumberOfDecimalExceededException e) {
            Assert.fail();
        }
    }

    @Test
    public void additionCommutativity(){
        try {
            BigInt b1 = new BigInt("123456");
            BigInt b2 = new BigInt("654321");
            BigInt b3 = new BigInt("111111");

            Assert.assertEquals(b1.add(b2).convertToString(), b2.add(b1).convertToString());
            Assert.assertEquals(b1.add(b2).add(b3).convertToString(), b3.add(b2).add(b1).convertToString());
        } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException e) {
            Assert.fail();
        }
    }

    @Test
    public void additionWithALotOfCarry(){
        try {
            BigInt b1 = new BigInt("123456");
            BigInt b2 = new BigInt("99999999999999999");

            Assert.assertEquals(b1.add(b2).convertToString(), "100000000000123455");
            Assert.assertEquals(b2.add(b1).convertToString(), "100000000000123455");
        } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException e) {
            Assert.fail();
        }
    }

    @Test
    public void addTwoNumbersAndThrowErrorBecauseNumberIsTooBig() throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {

        try {
            BigInt b1 = new BigInt(TestUtil.getRandomNumber(100_000, "9"));
            BigInt b2 = new BigInt(TestUtil.getRandomNumber(100_000, "9"));

            b1.add(b2);

            //Should not get here
            Assert.fail();
        } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException e) {
            Assert.assertTrue(true);
        }
    }

}