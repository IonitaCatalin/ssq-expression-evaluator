package com.evaluator.types.bigint;

import com.evaluator.types.BigInt;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;
import org.junit.Assert;
import org.junit.Test;
import util.TestUtil;

public class ConstructorTest {

    @Test
    public void createNewBigInt(){
        try {
            BigInt b1 = new BigInt();
            BigInt b2 = new BigInt("123456");
            BigInt b3 = new BigInt(b2);
            BigInt b4 = new BigInt(123456);

            int[] i = {1,2,3,4,5,6};
            BigInt b5 = new BigInt(i);


        } catch (InvalidNumberFormatException | MaximumNumberOfDecimalExceededException e) {
            Assert.fail();
        }
    }

    @Test
    public void shouldThrowErrorIfFormatIsNotCorrect(){
        try {

            BigInt b2 = new BigInt("123f456");

            //should not get here
            Assert.fail();
        } catch (InvalidNumberFormatException | MaximumNumberOfDecimalExceededException e) {
            Assert.assertEquals(e.getMessage(), "The string must contain only decimals");
        }
    }

    @Test
    public void shouldThrowErrorIfNumberOfDecimalsIsTooBig(){
        try {

            BigInt b2 = new BigInt(TestUtil.getRandomNumber(100_001));

            //should not get here
            Assert.fail();
        } catch (InvalidNumberFormatException | MaximumNumberOfDecimalExceededException e) {
            Assert.assertEquals(e.getMessage(), "Maximum number of decimals allowed is 100.000");
        }
    }
}
