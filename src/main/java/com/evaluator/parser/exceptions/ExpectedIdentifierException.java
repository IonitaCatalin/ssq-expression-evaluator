package com.evaluator.parser.exceptions;

public class ExpectedIdentifierException extends ParserException{
    public ExpectedIdentifierException(int row, int column) {
        super("Identifier expected at:" + row +"," + column, row, column);
    }
}
