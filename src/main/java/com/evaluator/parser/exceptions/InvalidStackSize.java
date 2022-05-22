package com.evaluator.parser.exceptions;

public class InvalidStackSize extends ParserException {
    public InvalidStackSize( int row, int column) {
        super("The execution was interrupted due to stack size limitation " + row + ", " + column, row, column);
    }
}
