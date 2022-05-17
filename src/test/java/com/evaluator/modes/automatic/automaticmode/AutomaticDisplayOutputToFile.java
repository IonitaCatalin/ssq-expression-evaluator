package com.evaluator.modes.automatic.automaticmode;

import com.evaluator.modes.automatic.AutomaticMode;
import com.evaluator.modes.automatic.JSONEntry;
import com.evaluator.types.BigInt;
import com.evaluator.types.exceptions.InvalidNumberFormatException;
import com.evaluator.types.exceptions.MaximumNumberOfDecimalExceededException;
import com.evaluator.values.Value;
import com.google.gson.internal.LinkedTreeMap;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AutomaticDisplayOutputToFile {
    @Test
    public void displayOutputToFile(){
        AutomaticMode automaticMode = new AutomaticMode();
        JSONEntry jsonEntry = new JSONEntry();
        jsonEntry.setExpr("(A+B)");
        Map<String,String> ltm = new LinkedTreeMap<>();
        ltm.put("A","10");
        ltm.put("B","10");
        try {
            jsonEntry.setResult(new Value(new BigInt("20")));
        } catch (MaximumNumberOfDecimalExceededException | InvalidNumberFormatException e) {
            Assert.fail();
        }
        jsonEntry.setAssigns(ltm);
        Map<String, JSONEntry> mapping = new HashMap<>();
        mapping.put("", jsonEntry);
        automaticMode.setMapping(mapping);
        automaticMode.displayOutputToFile();
        String expectedResult = "[{\"result\":{\"type\":\"NUMBER\",\"valueAsNumber\":{\"numberOfDigits\":2,\"value\":[0,2]}},\"expr\":\"(A+B)\",\"assigns\":{\"A\":\"10\",\"B\":\"10\"}}]";
        try {
            BufferedReader reader = Files.newBufferedReader(Paths.get("output.json"));
            String inFileText="";
            String line;
            while ((line = reader.readLine()) != null) {
                inFileText+= line;
            }
            Assert.assertEquals(expectedResult, inFileText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
