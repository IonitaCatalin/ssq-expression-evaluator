package com.evaluator.parser.exceptions;

public class InitializationExpectedException extends ParserException{
    public InitializationExpectedException(int row, int column) {
        super("Initialization expected at" + row + " ," + column, row, column);
    }
}
