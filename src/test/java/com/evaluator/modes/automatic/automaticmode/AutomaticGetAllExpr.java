package com.evaluator.modes.automatic.automaticmode;

import com.evaluator.modes.automatic.AutomaticMode;
import com.evaluator.modes.automatic.JSONEntry;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

public class AutomaticGetAllExpr {
    @Test
    public void getAllExpr(){
        AutomaticMode spyAutomaticMode = Mockito.spy(AutomaticMode.class);
        List<JSONEntry> jsonEntries = new ArrayList();
        JSONEntry jsonEntry = new JSONEntry();
        jsonEntry.setExpr("(A+B)");
        Map<String, String> myMap = new HashMap<>();
        myMap.put("A","100000000000");
        myMap.put("B","500000000000");
        jsonEntry.setAssigns(myMap);
        jsonEntries.add(jsonEntry);
        try {
            when(spyAutomaticMode.getJSONFromFile()).thenReturn(jsonEntries);
            List<String> expressionsToTest = new ArrayList<>();
            expressionsToTest.add("A=100000000000;B=500000000000;(A+B)");
            Assert.assertEquals(spyAutomaticMode.getAllExpr(), expressionsToTest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
