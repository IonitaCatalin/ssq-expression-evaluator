package com.evaluator.modes.automatic.automaticmode;

import com.evaluator.modes.RuntimeMode;
import com.evaluator.modes.automatic.AutomaticMode;
import com.evaluator.modes.automatic.JSONEntry;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class AutomaticGetJSONFromFile {
    @Test
    public void getJSONFromFile(){
        AutomaticMode automaticMode = new AutomaticMode();
        try {
            List<JSONEntry> jsonEntryList = automaticMode.getJSONFromFile();
            Assert.assertEquals(jsonEntryList.size(), 5);
        } catch (IOException e) {
            Assert.fail();
        }
    }
}
