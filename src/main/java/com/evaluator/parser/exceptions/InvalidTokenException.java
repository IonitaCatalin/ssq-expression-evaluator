package com.evaluator.parser.exceptions;

public class InvalidTokenException extends ParserException{
    public InvalidTokenException( int row, int column) {
        super("Invalid token at " + row + ", " + column, row, column);
    }
}
