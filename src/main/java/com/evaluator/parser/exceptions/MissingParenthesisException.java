package com.evaluator.parser.exceptions;

public class MissingParenthesisException extends ParserException{
    public MissingParenthesisException(int row, int column) {
        super("Missing parenthesis at" + row + ", "+ column, row, column);
    }
}
