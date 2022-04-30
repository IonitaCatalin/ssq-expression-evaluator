package com.evaluator;

import com.evaluator.parser.exceptions.ParserException;
import com.evaluator.parser.Parser;
import com.evaluator.tokens.Token;
import com.evaluator.values.Value;

import java.util.List;

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

    public static void main(String[] args) throws ParserException {
        boolean verbose = false;
        String expression = null;
        if (args.length == 0) {
            usage();
            return;
        } else {
            expression = args[0];
            verbose = args.length > 1 && args[1].toLowerCase().startsWith("-v");
        }

        DemoParser parser = new DemoParser();
        Value value = parser.evaluate(expression);

        System.out.println();
        System.out.println("EVALUATING " + expression);

        if (verbose) {
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
        }

        System.out.print("The result of evaluating the application" + value);

    }

}