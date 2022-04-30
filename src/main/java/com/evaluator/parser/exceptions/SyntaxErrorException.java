package com.evaluator.parser.exceptions;

public class SyntaxErrorException extends ParserException{
    public SyntaxErrorException(int row, int column) {
        super("Syntax error " + row + "," + column, row, column);
    }
}
