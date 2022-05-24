package com.evaluator.tokens;

import com.evaluator.operators.Operator;
import com.evaluator.utils.Conditions;
import com.evaluator.types.BigInt;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;
import com.evaluator.values.Value;
import com.evaluator.values.ValueType;

public class Token {
    private final TokenType type;

    private final Value value;

    private final int row;
    private final int column;

    private String text;

    public Token(TokenType type, String text, int row, int column)
            throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {
        this(type, text, null, row, column);
        Value value = new Value();
        if (type != null) {
            if (TokenType.NUMBER.equals(type)) {
                value = new Value(new BigInt(text));
            }
        }
        setValue(value);
    }

    public Token() {
        type = TokenType.NUMBER;
        value = new Value();
        row = 0;
        column = 0;
        text = "";

        value.setName("");
        value.setType(ValueType.UNDEFINED);
        value.setValue(new BigInt(0));
    }

    public Token(TokenType type, String text, Value value, int row, int column)
            throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {

        // Preconditions
        assert row >= 0;
        assert column >= 0;

        this.type = type == null ? TokenType.NOMATCH : type;
        this.text = text;
        this.row = row;
        this.column = column;
        this.value = new Value(value);


    }


    public BigInt asNumber() {
        return getValue() != null ? getValue().asNumber() : null;
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


    public void setValue(Value value)
            throws InvalidNumberFormatException,
            MaximumNumberOfDecimalExceededException {

        this.value.set(value);

    }


    public void setText(String text) {
        this.text = text;

        assert Conditions.areEqual(this.text, text);
    }

    public boolean operatorEquals(Operator... operators) {

        assert Conditions.isNotNull(operators);

        boolean result = false;
        if (text != null) {
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
