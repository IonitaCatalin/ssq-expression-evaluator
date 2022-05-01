package com.evaluator;

import com.evaluator.modes.Mode;
import com.evaluator.modes.RuntimeMode;
import com.evaluator.modes.automatic.AutomaticMode;
import com.evaluator.modes.interactive.InteractiveMode;
import com.evaluator.parser.exceptions.ParserException;
import com.evaluator.parser.Parser;
import com.evaluator.tokens.Token;
import com.evaluator.types.exceptions.DivisionByZeroException;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;
import com.evaluator.types.exceptions.NegativeValueException;
import com.evaluator.values.Value;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static class DemoParser extends Parser {
        private List<Token> infix;
        private List<Token> rpn;

        public DemoParser() {
            super();
        }

        public List<Token> getInfix() {
            return infix;
        }

        public List<Token> getPostfix() {
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
        selectedMode.solve();
    }
}