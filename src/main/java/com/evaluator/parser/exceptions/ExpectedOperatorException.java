package com.evaluator.parser.exceptions;

public class ExpectedOperatorException extends ParserException{
    public ExpectedOperatorException( int row, int column) {
        super("Operator expected at " + row + " ," + column, row, column);
    }
}

