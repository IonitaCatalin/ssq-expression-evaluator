package com.evaluator;

import com.evaluator.modes.Mode;
import com.evaluator.modes.RuntimeMode;
import com.evaluator.modes.automatic.AutomaticMode;
import com.evaluator.modes.interactive.InteractiveMode;
import com.evaluator.parser.exceptions.ParserException;
import com.evaluator.parser.Parser;
import com.evaluator.tokens.Token;

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

    public static void main(String[] args)  {
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
        selectedMode.solve();
    }
}