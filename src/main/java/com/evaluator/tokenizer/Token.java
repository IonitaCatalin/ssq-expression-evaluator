package com.evaluator.tokenizer;

import java.math.BigDecimal;
public class Token {
    private final TokenType type;

    private Value value;
    private Value orgValue;

    private final int row;
    private final int column;
    private String text;

    private int argc;

    public Token(TokenType type, String text, int row, int column) {
        this(type, text, null, row, column);
        Value value = new Value();
        if (type != null) {
            if (TokenType.NUMBER.equals(type)) {
                value = new Value(new BigDecimal(text));
            }
        }
        setValue(value);

    }

    public Token(TokenType type, Value value, int row, int column) {
        this(type, "VALUE", value, row, column);
    }

    public Token(TokenType type, String text, Value value, int row, int column) {
        this.type = type == null ? TokenType.NOMATCH : type;
        this.text = text;
        this.row = row;
        this.column = column;
        this.value = new Value(value);
    }

    public String asString() {
        return getValue() != null ? getValue().asString() : null;
    }

    public void saveOrgValue() {
        orgValue = new Value(value);
    }

    public void restoreOrgValue() {
        value.set(orgValue);
    }


    public BigDecimal asNumber() {
        return getValue() != null ? getValue().asNumber() : null;
    }

    public boolean equals(Operator operator) {
        return operator != null && getText() != null && getText().equalsIgnoreCase(operator.getText());
    }


    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public String getText() {
        return text;
    }

    public TokenType getType() {
        return type;
    }

    public Value getValue() {
        return this.value;
    }

    public int getArgc() {
        return argc;
    }

    public boolean isConstant() {
        return TokenType.CONSTANT.equals(type);
    }

    public boolean isIdentifier() {
        return TokenType.IDENTIFIER.equals(type);
    }

    public boolean isNumber() {
        return TokenType.NUMBER.equals(type);
    }

    public boolean isOperator() {
        if (TokenType.OPERATOR.equals(type)) {
            Operator op = Operator.find(this);
            if (op == null) {
                op = Operator.find(this);
            }
            return !Operator.L_PARENTHESIS.equals(op) && !Operator.R_PARENTHESIS.equals(op);
        } else {
            return false;
        }
    }

    public boolean isParen() {
        Operator op = Operator.find(this);
        if (op == null) {
            op = Operator.find(this);
        }
        return TokenType.OPERATOR.equals(type) && Operator.L_PARENTHESIS.equals(op);
    }

    public Token setArgc(int argc) {
        this.argc = argc;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setValue(Value value) {
        this.value.set(value);
    }


    public boolean opEquals(Operator... operators) {
        boolean result = false;
        if (text != null && operators != null) {
            for (Operator operator : operators) {
                result = operator != null && text.equals(operator.getText());
                if (result) {
                    break;
                }
            }
        }
        return result;
    }
    @Override
    public String toString() {
        return "type = " +
                this.getType() +
                " value = " + this.getValue().asNumber() +
                " token = " + this.getText();
    }
}
