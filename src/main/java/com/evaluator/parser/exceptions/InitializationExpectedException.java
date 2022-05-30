package com.evaluator.parser.exceptions;

/**
 * The exception class for initialization expected exception error
 *
 * @author Ionita Mihail-Catalin
 */
public class InitializationExpectedException extends ParserException{
    public InitializationExpectedException(int row, int column) {
        super("Initialization expected at" + row + " ," + column, row, column);
    }
}
