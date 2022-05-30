package com.evaluator.parser.exceptions;

/**
 * The exception class for parenthesis exception error
 *
 * @author Ionita Mihail-Catalin
 */
public class ParenthesisCountException extends ParserException{
    public ParenthesisCountException(int row, int column) {
        super("Missing parenthesis at" + row + ", "+ column, row, column);
    }
}
