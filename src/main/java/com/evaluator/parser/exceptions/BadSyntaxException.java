package com.evaluator.parser.exceptions;
/**
 * The exception class for bad syntax error
 *
 * @author Ionita Mihail-Catalin
 */
public class BadSyntaxException extends ParserException{
    public BadSyntaxException(int row, int column) {
        super("Syntax error " + row + "," + column, row, column);
    }
}
