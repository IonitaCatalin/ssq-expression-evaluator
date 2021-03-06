package com.evaluator.modes;

import com.evaluator.Main;
import com.evaluator.parser.exceptions.ParserException;
import com.evaluator.tokens.Token;
import com.evaluator.types.exceptions.DivisionByZeroException;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;
import com.evaluator.types.exceptions.NegativeValueException;
import com.evaluator.values.Value;

import java.util.List;

/**
 * High Level abstract solver for both automatic and interactive
 * Given an expression and mapped values get the result of the computation
 * The mode enum, used for representing the available modes for the solver
 * @author Popa Stefan
 * @since 01.05.2022
 */
public abstract class AbstractSolver {
    public Value solveExpression(String expression)
            throws ParserException,
            InvalidNumberFormatException,
            MaximumNumberOfDecimalExceededException,
            NegativeValueException,
            DivisionByZeroException
    {
        assert expression != null;
        Main.DemoParser parser = new Main.DemoParser();
        Value value = parser.evaluate(expression);

        List<Token> tokens = parser.getInfix();
        System.out.println("Infixed form:");
        for (Token token : tokens) {
            System.out.println(token);
        }
        System.out.println();

        List<Token> rpn = parser.getPostfix();
        System.out.println("RPN form:");
        for (Token token : rpn) {
            System.out.println(token);
        }
        System.out.println();
        System.out.print("The result of evaluating the application" + value);
        assert value != null;
        return value;
    }
}
