package com.evaluator.values;

import com.evaluator.types.BigInt;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;

import java.util.Objects;

public class Value {

    private ValueType type = ValueType.UNDEFINED;

    private BigInt valueAsNumber = null;
    private String valueAsString = null;

    public Value() {}

    public Value(BigInt var) {
        setValue(var);
    }

    public Value(String valueAsString) {
        this.valueAsString = valueAsString;
        this.valueAsNumber = null;
    }

    public Value(Value var)
            throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {
        set(var);
    }

    public final Value clear() {
        this.type = ValueType.UNDEFINED;
        this.valueAsNumber = null;
        return this;
    }

    public final void set(Value var)
            throws InvalidNumberFormatException, MaximumNumberOfDecimalExceededException {
        if (var != null) {
            this.type = var.type;
            this.valueAsString = var.valueAsString;
            this.valueAsNumber = var.valueAsNumber == null ? null : new BigInt(var.valueAsNumber.toString());
        }
    }

    public String asString() {
        return this.valueAsString;
    }

    public ValueType getType() {
        return type;
    }

    public void setType(ValueType type) {
        this.type = type;
    }

    public BigInt asNumber() {
        return valueAsNumber;
    }


    public void setValue(BigInt value) {
        this.valueAsNumber = value;
        setType(ValueType.NUMBER);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("type = ");
        if (type == ValueType.NUMBER) {
            sb.append(type.name());
        } else {
            sb.append("UNDEFINED");
        }
        sb.append(" | value = ").append(valueAsNumber);
        sb.append(" | text = ").append(Objects.equals(valueAsString, "") ? "(nothing)" : valueAsString);
        return sb.toString();
    }

}
