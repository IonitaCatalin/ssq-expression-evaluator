package com.evaluator.modes.automatic;

import com.evaluator.modes.RuntimeMode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AutomaticMode implements RuntimeMode {

    public AutomaticMode() {}

    @Override
    public List<String> getAllExpr() {
        List<JSONEntry> jsonEntries;
        try {
            jsonEntries = getJSONFromFile();
            List<String> expressions = new ArrayList<>();
            for (JSONEntry entry : jsonEntries) {
                expressions.add(entry.getExpressionInput());
            }
            return expressions;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading from file...");
            return null;
        }
    }

    private static List<JSONEntry> getJSONFromFile() throws IOException {
        Reader reader = Files.newBufferedReader(Paths.get("input.json"));
        Gson gson = new Gson();
        Type userListType = new TypeToken<ArrayList<JSONEntry>>() {}.getType();
        List<JSONEntry> jsonEntries = gson.fromJson(reader, userListType);
        return jsonEntries;
    }
}
