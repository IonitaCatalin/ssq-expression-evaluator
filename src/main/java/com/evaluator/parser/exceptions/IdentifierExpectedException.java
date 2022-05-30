package com.evaluator.parser.exceptions;

/**
 * The exception class for identifier exception error
 *
 * @author Ionita Mihail-Catalin
 */
public class IdentifierExpectedException extends ParserException{
    public IdentifierExpectedException(int row, int column) {
        super("Identifier expected at:" + row +"," + column, row, column);
    }
}
