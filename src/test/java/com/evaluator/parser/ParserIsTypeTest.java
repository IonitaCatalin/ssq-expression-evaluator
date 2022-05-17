package com.evaluator.parser;

import com.evaluator.operators.Operator;
import com.evaluator.parser.exceptions.OperatorExpectedException;
import com.evaluator.parser.exceptions.OperatorNotFoundException;
import com.evaluator.parser.exceptions.ParserException;
import com.evaluator.tokens.Token;
import org.junit.Test;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.*;

public class ParserIsTypeTest {

/*    @Test(expected= OperatorExpectedException.class)
    public void testIsTypeShouldThrowOperatorExpected() throws ParserException {
        Parser parser = new Parser();
        Token mockedToken = mock(Token.class);

        when(mockedToken.isOperator()).thenReturn(false);
        parser.isType(mockedToken, 1);

    }

    @Test(expected = OperatorNotFoundException.class)
    public void testIsTypeShouldThrowOperatorNotFound() throws ParserException {
        Parser parser = new Parser();
        Token mockedToken = mock(Token.class);

        MockedStatic<Operator> mockedOperator = mockStatic(Operator.class);
        mockedOperator.when(() -> Operator.find(any())).thenReturn(null);

        when(mockedToken.isOperator()).thenReturn(true);
        parser.isType(mockedToken, 1);

        mockedOperator.close();

    }*/
}
