package com.evaluator.parser;

import com.evaluator.operators.Operator;
import com.evaluator.parser.exceptions.ParserException;
import com.evaluator.tokens.Token;
import com.evaluator.tokens.TokenType;
import com.evaluator.types.BigInt;
import com.evaluator.types.exceptions.DivisionByZeroException;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;
import com.evaluator.types.exceptions.NegativeValueException;
import com.evaluator.values.Value;
import com.evaluator.values.ValueType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockedStatic;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ParserProcessOperatorsTest {

    private MockedStatic<Operator> mockedOperator;

    @Before
    public void setUp() {
        this.mockedOperator = mockStatic(Operator.class);
    }

    @After
    public void after() {
        mockedOperator.close();
    }

    @Test
    public void testProcessOperatorForPlusOperator() {
        Parser parser = new Parser();
        try {
            Token token = new Token(TokenType.OPERATOR, "+", 0,0 );
            Token lhs = new Token(TokenType.NUMBER, "20", 0,1);
            Token rhs = new Token(TokenType.NUMBER, "20", 0,2);

            BigInt spyOnInteger = mock(BigInt.class);

            this.mockedOperator.when(() -> Operator.find(any())).thenReturn(Operator.PLUS);

            when(spyOnInteger.add(any())).thenReturn(new BigInt(20));

            Stack<Token> stack = new Stack<>();

            stack.add(lhs);
            stack.add(rhs);

            Token result = parser.processOperators(token, stack);

            assertEquals(result.getType(), TokenType.NUMBER);
            assertEquals(result.getValue().asNumber(), new BigInt(40));

        } catch (InvalidNumberFormatException | MaximumNumberOfDecimalExceededException | ParserException | DivisionByZeroException e) {
            fail();
        }
    }

    @Test
    public void testProcessOperatorForMinusOperator() {
        Parser parser = new Parser();
        try {
            Token token = new Token(TokenType.OPERATOR, "-", 0,0 );
            Token lhs = new Token(TokenType.NUMBER, "20", 0,1);
            Token rhs = new Token(TokenType.NUMBER, "20", 0,2);

            BigInt spyOnInteger = mock(BigInt.class);

            this.mockedOperator.when(() -> Operator.find(any())).thenReturn(Operator.MINUS);
            when(spyOnInteger.subtract(any())).thenReturn(new BigInt(0));

            Stack<Token> stack = new Stack<>();

            stack.add(lhs);
            stack.add(rhs);

            Token result = parser.processOperators(token, stack);

            assertEquals(result.getType(), TokenType.NUMBER);
            assertEquals(result.getValue().asNumber(), new BigInt());


        } catch (InvalidNumberFormatException | MaximumNumberOfDecimalExceededException | ParserException | DivisionByZeroException | NegativeValueException e) {
            fail();
        }
    }

    @Test
    public void testProcessOperatorForMultOperator() {
        Parser parser = new Parser();
        try {
            Token token = new Token(TokenType.OPERATOR, "*", 0,0 );
            Token lhs = new Token(TokenType.NUMBER, "2", 0,1);
            Token rhs = new Token(TokenType.NUMBER, "2", 0,2);

            BigInt spyOnInteger = mock(BigInt.class);

            this.mockedOperator.when(() -> Operator.find(any())).thenReturn(Operator.MULT);
            when(spyOnInteger.multiply(any())).thenReturn(new BigInt(4));

            Stack<Token> stack = new Stack<>();

            stack.add(lhs);
            stack.add(rhs);

            Token result = parser.processOperators(token, stack);

            assertEquals(result.getType(), TokenType.NUMBER);
            assertEquals(result.getValue().asNumber(), new BigInt(4));

        } catch (ParserException | InvalidNumberFormatException | DivisionByZeroException | MaximumNumberOfDecimalExceededException e) {
            fail();
        }
    }

    @Test
    public void testProcessOperatorForDivOperator() {
        Parser parser = new Parser();
        try {
            Token token = new Token(TokenType.OPERATOR, "/", 0,0 );
            Token lhs = new Token(TokenType.NUMBER, "2", 0,1);
            Token rhs = new Token(TokenType.NUMBER, "2", 0,2);

            BigInt spyOnInteger = mock(BigInt.class);

            this.mockedOperator.when(() -> Operator.find(any())).thenReturn(Operator.DIV);
            when(spyOnInteger.multiply(any())).thenReturn(new BigInt(1));

            Stack<Token> stack = new Stack<>();

            stack.add(lhs);
            stack.add(rhs);

            Token result = parser.processOperators(token, stack);

            assertEquals(result.getType(), TokenType.NUMBER);
            assertEquals(result.getValue().asNumber(), new BigInt(1));


        } catch (ParserException | InvalidNumberFormatException | DivisionByZeroException | MaximumNumberOfDecimalExceededException e) {
            fail();
        }
    }

    @Test
    public void testProcessOperatorForPowOperator() {
        Parser parser = new Parser();
        try {
            Token token = new Token(TokenType.OPERATOR, "^", 0,0 );
            Token lhs = new Token(TokenType.NUMBER, "2", 0,1);
            Token rhs = new Token(TokenType.NUMBER, "2", 0,2);

            BigInt spyOnInteger = mock(BigInt.class);

            this.mockedOperator.when(() -> Operator.find(any())).thenReturn(Operator.POW);
            when(spyOnInteger.multiply(any())).thenReturn(new BigInt(4));

            Stack<Token> stack = new Stack<>();

            stack.add(lhs);
            stack.add(rhs);

            Token result = parser.processOperators(token, stack);

            System.out.println(result);

            assertEquals(result.getType(), TokenType.NUMBER);
            assertEquals(result.getValue().asNumber(), new BigInt(4));

        } catch (ParserException | InvalidNumberFormatException | DivisionByZeroException | MaximumNumberOfDecimalExceededException e) {
            fail();
        }
    }

    @Test
    public void testProcessOperatorForSqrtOperator() {
        Parser parser = new Parser();
        try {
            Token token = new Token(TokenType.OPERATOR, "#", 0,0 );
            Token lhs = new Token(TokenType.NUMBER, "4", 0,1);
            Token rhs = new Token(TokenType.NUMBER, "2", 0,2);

            BigInt spyOnInteger = mock(BigInt.class);

            this.mockedOperator.when(() -> Operator.find(any())).thenReturn(Operator.SQRT);
            when(spyOnInteger.multiply(any())).thenReturn(new BigInt(2));

            Stack<Token> stack = new Stack<>();

            stack.add(lhs);
            stack.add(rhs);

            Token result = parser.processOperators(token, stack);


            assertEquals(result.getType(), TokenType.NUMBER);
            assertEquals(result.getValue().asNumber(), new BigInt(2));

        } catch (ParserException | InvalidNumberFormatException | DivisionByZeroException | MaximumNumberOfDecimalExceededException e) {
            fail();
        }
    }

//    @Test
//    public void testProcessOperatorForAsgnOperator() {
//        Parser parser = new Parser();
//        Map<String, Value> variables = new HashMap<>();
//        Value value = new Value();
//
//        value.setType(ValueType.UNDEFINED);
//        variables.put("a", value);
//
//        parser.setVariables(variables);
//
//        try {
//            Token token = new Token(TokenType.OPERATOR, "=", 0,0 );
//            Token lhs = new Token(TokenType.IDENTIFIER, "a", 0,1);
//            Token rhs = new Token(TokenType.NUMBER, "2", 0,2);
//
//            BigInt spyOnInteger = mock(BigInt.class);
//            Token spyOnParser = mock(Token.class);
//
//            this.mockedOperator.when(() -> Operator.find(any())).thenReturn(Operator.ASSIGNMENT);
//
//            when(spyOnParser.isIdentifier()).thenReturn(true);
//            when(spyOnInteger.multiply(any())).thenReturn(new BigInt(4));
//
//            Stack<Token> stack = new Stack<>();
//
//            stack.add(lhs);
//            stack.add(rhs);
//
//            Token result = parser.processOperators(token, stack);
//
//            assertNull(result);
//
//        } catch (ParserException | InvalidNumberFormatException | DivisionByZeroException | MaximumNumberOfDecimalExceededException e) {
//            fail();
//        }
//    }
}
