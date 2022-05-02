package com.evaluator.parser.exceptions;

public class IdentifierExpectedException extends ParserException{
    public IdentifierExpectedException(int row, int column) {
        super("Identifier expected at:" + row +"," + column, row, column);
    }
}
