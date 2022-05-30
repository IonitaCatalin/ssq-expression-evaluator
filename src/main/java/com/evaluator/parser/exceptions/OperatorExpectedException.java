package com.evaluator.parser.exceptions;

/**
 * The exception class for operator expected exception error
 *
 * @author Ionita Mihail-Catalin
 */
public class OperatorExpectedException extends ParserException{
    public OperatorExpectedException(int row, int column) {
        super("Operator expected at" + row + ", "+ column, row, column);
    }
}
