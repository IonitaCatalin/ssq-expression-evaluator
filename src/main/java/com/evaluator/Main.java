package com.evaluator;

import com.evaluator.modes.Mode;
import com.evaluator.modes.RuntimeMode;
import com.evaluator.modes.automatic.AutomaticMode;
import com.evaluator.modes.automatic.JSONEntry;
import com.evaluator.modes.interactive.InteractiveMode;
import com.evaluator.parser.exceptions.ParserException;
import com.evaluator.parser.Parser;
import com.evaluator.tokens.Token;
import com.evaluator.types.exceptions.DivisionByZeroException;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;
import com.evaluator.types.exceptions.NegativeValueException;
import com.evaluator.values.Value;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static class DemoParser extends Parser {
        private List<Token> infix;
        private List<Token> rpn;

        DemoParser() {
            super();
        }

        List<Token> getInfix() {
            return infix;
        }

        List<Token> getPostfix() {
            return rpn;
        }

        @Override
        public List<Token> generateRPN(List<Token> inputTokens) throws ParserException {
            infix = inputTokens;
            rpn = super.generateRPN(inputTokens);
            return rpn;
        }
    }

    private static void usage() {
        System.out.println();
        System.out.println("usage: Demo \"expression\" [-verbose]");
    }

    public static void main(String[] args)
            throws ParserException,
            InvalidNumberFormatException,
            MaximumNumberOfDecimalExceededException,
            NegativeValueException, DivisionByZeroException {
        Mode modeType = Mode.AUTOMATIC;
        RuntimeMode selectedMode = null;
        Scanner scanner = new Scanner(System.in);
        String promptMode;
        System.out.println("Choose mode\n1-Automatic\n2-Interactive");
        promptMode = scanner.nextLine();
        if (promptMode.startsWith("1")) {
            selectedMode = new AutomaticMode();
        } else {
            selectedMode = new InteractiveMode();
        }
        if (selectedMode == null) {
            return;
        }
        List<String> expressions = selectedMode.getAllExpr();

        for (String currentExpr : expressions) {
            solveExpression(currentExpr);
        }
    }

    private static void solveExpression(String expression) throws ParserException, InvalidNumberFormatException, MaximumNumberOfDecimalExceededException, NegativeValueException, DivisionByZeroException {
        DemoParser parser = new DemoParser();
        Value value = parser.evaluate(expression);

        System.out.println();
        System.out.println("EVALUATING " + expression);

        System.out.println();

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
    }


}