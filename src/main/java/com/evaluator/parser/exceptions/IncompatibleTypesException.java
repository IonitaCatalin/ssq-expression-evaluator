package com.evaluator.parser.exceptions;

public class IncompatibleTypesException extends ParserException{
    public IncompatibleTypesException(int row, int column) {
        super("Incompatible types for operations at" + row + ", " + column, row, column);
    }
}
