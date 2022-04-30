package com.evaluator.parser.exceptions;

public class ExpectedInitializationException extends ParserException{
    public ExpectedInitializationException(int row, int column) {
        super("Initialization expected at" + row + " ," + column, row, column);
    }
}
