package com.evaluator.modes.interactive;

import com.evaluator.modes.AbstractSolver;
import com.evaluator.modes.Mode;
import com.evaluator.modes.RuntimeMode;
import com.evaluator.parser.exceptions.ParserException;
import com.evaluator.types.exceptions.DivisionByZeroException;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;
import com.evaluator.types.exceptions.NegativeValueException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InteractiveMode extends AbstractSolver implements RuntimeMode {

    @Override
    public List<String> getAllExpr() {
        Scanner scanner = new Scanner(System.in);
        String input;
        System.out.println("Enter expression; for example: A=2;B=3;(A+B)");
        input = scanner.nextLine();
        List<String> expressions = new ArrayList<>();
        expressions.add(input);
        return expressions;
    }

    @Override
    public void solve() {
        List<String> expressions = getAllExpr();
        for(String currentExpr: expressions){
            try {
                solveExpression(currentExpr);
            } catch (ParserException | InvalidNumberFormatException | MaximumNumberOfDecimalExceededException | NegativeValueException | DivisionByZeroException e) {
                e.printStackTrace();
            }
        }
    }
}
