package com.evaluator.parser.exceptions;

/**
 * The exception class for invalid stack exception error
 *
 * @author Ionita Mihail-Catalin
 */
public class InvalidStackSize extends ParserException {
    public InvalidStackSize( int row, int column) {
        super("The execution was interrupted due to stack size limitation " + row + ", " + column, row, column);
    }
}
