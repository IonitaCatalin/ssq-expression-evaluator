package com.evaluator.values;

import com.evaluator.utils.Conditions;
import com.evaluator.types.BigInt;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;

import java.util.Objects;

/**
 * @description Value class, holds the textual and integer representation
 * of a token.
 *
 * @author Ionita Mihail-Catalin
 * @since 01.05.2022
 */
public class Value {


    /**
     * The type of the value, UNDEFINED by default
     */
    private ValueType type = ValueType.UNDEFINED;

    /**
     * The name of the value
     */
    private String name;

    /**
     * The value as a BigInt
     */
    private BigInt valueAsNumber = null;

    /**
     * The value as a String
     */
    private String valueAsString = null;

    public Value() {

    }

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
            throws InvalidNumberFormatException,
            MaximumNumberOfDecimalExceededException
    {
        if (var != null) {
            this.type = var.type;
            this.name = var.name;
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

        assert Conditions.areEqual(this.valueAsNumber, value);
        assert Conditions.areEqual(this.type, ValueType.NUMBER);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

        assert Conditions.areEqual(this.name, name);
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
