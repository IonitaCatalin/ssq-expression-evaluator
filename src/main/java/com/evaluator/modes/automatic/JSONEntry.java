package com.evaluator.modes.automatic;

import com.evaluator.values.Value;

import java.util.Map;

public class JSONEntry {
    private Value result;

    public void setResult(Value result) {
        this.result = result;
    }

    private String expr;
    private Map<String, String> assigns;

    public JSONEntry() {
    }

    public void setExpr(String expr) {
        this.expr = expr;
    }

    public Map<String, String> getAssigns() {
        return assigns;
    }

    public void setAssigns(Map<String, String> assigns) {
        this.assigns = assigns;
    }

    public String getExpressionInput() {
        if (expr == null || assigns == null) {
            return "";
        }
        String exprInput = "";
        for (var entry : assigns.entrySet()) {
            exprInput += entry.getKey() + "=" + entry.getValue() + ";";
        }
        exprInput += expr;
        return exprInput;
    }

    @Override
    public String toString() {
        return "JSONEntry{" +
                "expr='" + expr + '\'' +
                ", assigns=" + assigns +
                '}';
    }
}
