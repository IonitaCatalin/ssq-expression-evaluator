package com.evaluator.tokenizer;

import java.math.BigDecimal;
import java.util.Objects;

public class Value {

    private ValueType type = ValueType.UNDEFINED;
    private BigDecimal valueAsNumber = BigDecimal.ZERO;
    private String valueAsString = null;

    public Value() {}

    public Value(Object var) {
        if (var instanceof BigDecimal) {
            setValue((BigDecimal) var);
        } else {
            setValue(null);
        }
    }

    public Value(String valueAsString) {
        this.valueAsString = valueAsString;
        this.valueAsNumber = null;
    }

    public Value(Value var) {
        set(var);
    }

    public final Value clear() {
        this.type = ValueType.UNDEFINED;
        this.valueAsNumber = BigDecimal.ZERO;
        return this;
    }

    public final void set(Value var) {
        if (var != null) {
            this.type = var.type;
            this.valueAsString = var.valueAsString;
            this.valueAsNumber = var.valueAsNumber == null ? null : new BigDecimal(var.valueAsNumber.toPlainString());
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

    public BigDecimal asNumber() {
        return valueAsNumber;
    }


    public void setValue(BigDecimal value) {
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
