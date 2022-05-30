package com.evaluator.parser.exceptions;

/**
 * The exception class for invalid token exception error
 *
 * @author Ionita Mihail-Catalin
 */
public class InvalidTokenException extends ParserException{
    public InvalidTokenException( int row, int column) {
        super("Invalid token at " + row + ", " + column, row, column);
    }
}
