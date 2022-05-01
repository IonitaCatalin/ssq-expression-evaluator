package com.evaluator.modes.interactive;

import com.evaluator.modes.RuntimeMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InteractiveMode implements RuntimeMode {
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
}
