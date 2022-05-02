package com.evaluator.parser.exceptions;

public class ParenthesisCountException extends ParserException{
    public ParenthesisCountException(int row, int column) {
        super("Missing parenthesis at" + row + ", "+ column, row, column);
    }
}
