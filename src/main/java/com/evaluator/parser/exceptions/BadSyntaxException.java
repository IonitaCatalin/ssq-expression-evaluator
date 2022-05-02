package com.evaluator.parser.exceptions;

public class BadSyntaxException extends ParserException{
    public BadSyntaxException(int row, int column) {
        super("Syntax error " + row + "," + column, row, column);
    }
}
