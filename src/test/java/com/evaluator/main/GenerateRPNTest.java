package com.evaluator.main;

import com.evaluator.Main;
import com.evaluator.parser.exceptions.ParserException;
import com.evaluator.tokens.Token;
import com.evaluator.tokens.TokenType;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class GenerateRPNTest {

    @Test
    public void generateRPN(){
        Main.DemoParser parser = Mockito.mock(Main.DemoParser.class);
        try {
            List<Token> tokenList = new ArrayList<>();
            tokenList.add(new Token());
            tokenList.add(new Token(TokenType.OPERATOR, "+", 1,1));
            tokenList.add(new Token());
            Mockito.when(parser.generateRPN(Mockito.anyList())).thenReturn(tokenList);

            Assert.assertEquals(parser.generateRPN(tokenList), tokenList);

        } catch (ParserException | InvalidNumberFormatException | MaximumNumberOfDecimalExceededException e) {
            Assert.fail();
        }
    }
}
