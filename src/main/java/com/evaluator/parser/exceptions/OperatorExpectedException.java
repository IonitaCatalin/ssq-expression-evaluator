package com.evaluator.parser.exceptions;

public class OperatorExpectedException extends ParserException{
    public OperatorExpectedException(int row, int column) {
        super("Operator expected at" + row + ", "+ column, row, column);
    }
}
