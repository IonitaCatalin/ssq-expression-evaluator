package com.evaluator.modes.automatic;

import com.evaluator.modes.AbstractSolver;
import com.evaluator.modes.Mode;
import com.evaluator.modes.RuntimeMode;
import com.evaluator.parser.exceptions.ParserException;
import com.evaluator.types.exceptions.DivisionByZeroException;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;
import com.evaluator.types.exceptions.NegativeValueException;
import com.evaluator.values.Value;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutomaticMode extends AbstractSolver implements RuntimeMode {
    private List<JSONEntry> jsonEntries;
    private Map<String, JSONEntry> mapping;
    private static Gson gson;

    public AutomaticMode() {
        gson = new Gson();
        mapping = new HashMap<>();
    }

    @Override
    public List<String> getAllExpr() {
        try {
            jsonEntries = getJSONFromFile();
            List<String> expressions = new ArrayList<>();
            for (JSONEntry entry : jsonEntries) {
                String expressionInput = entry.getExpressionInput();
                expressions.add(expressionInput);
                mapping.put(expressionInput, entry);
            }
            return expressions;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading from file...");
            return null;
        }
    }

    public void displayOutputToFile() {
        if (mapping == null) {
            return;
        }
        Collection<JSONEntry> entries = mapping.values();
        String json = gson.toJson(entries);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter("output.json"));
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error writing to file...");
        }

    }

    @Override
    public void solve() {
        List<String> expressions = getAllExpr();
        for (String currentExpr : expressions) {
            try {
                Value result = solveExpression(currentExpr);
                mapping.get(currentExpr).setResult(result);
            } catch (ParserException | DivisionByZeroException | NegativeValueException | MaximumNumberOfDecimalExceededException | InvalidNumberFormatException e) {
                e.printStackTrace();
            }
        }
        displayOutputToFile();
    }

    public List<JSONEntry> getJSONFromFile() throws IOException {
        Reader reader = Files.newBufferedReader(Paths.get("input.json"));
        Type userListType = new TypeToken<ArrayList<JSONEntry>>() {
        }.getType();
        List<JSONEntry> jsonEntries = gson.fromJson(reader, userListType);
        return jsonEntries;
    }
}
