package com.evaluator.parser.exceptions;

/**
 * The exception class for parser exception error
 *
 * @author Ionita Mihail-Catalin
 */
public class ParserException extends Exception {

    private int row;
    private int col;

    public ParserException(String msg, int row, int column) {
        super(msg);
        initialize(row, column);
    }

    public ParserException(String msg, Throwable th, int row, int column) {
        super(msg, th);
        initialize(row, column);
    }

    private void initialize(int row, int column) {
        this.row = row;
        this.col = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return col;
    }

}
