package com.evaluator.parser.exceptions;

public class OperatorNotFoundException extends ParserException{
    public OperatorNotFoundException(int row, int column) {
        super("Operator not found" + row + ", " + column, row, column);
    }
}
