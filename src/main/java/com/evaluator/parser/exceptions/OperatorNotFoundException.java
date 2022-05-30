package com.evaluator.parser.exceptions;

/**
 * The exception class for operator not found exception error
 *
 * @author Ionita Mihail-Catalin
 */
public class OperatorNotFoundException extends ParserException{
    public OperatorNotFoundException(int row, int column) {
        super("Operator not found" + row + ", " + column, row, column);
    }
}
