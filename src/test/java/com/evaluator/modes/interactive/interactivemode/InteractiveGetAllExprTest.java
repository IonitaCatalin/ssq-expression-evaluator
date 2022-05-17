package com.evaluator.modes.interactive.interactivemode;

import com.evaluator.modes.RuntimeMode;
import com.evaluator.modes.interactive.InteractiveMode;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class InteractiveGetAllExprTest {
    @Test
    public void getAllExpr(){

        RuntimeMode interactiveMode = new InteractiveMode();
        String input = "A=2;B=3;(A+B)";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        List<String> expressions = new ArrayList<>();
        expressions.add("A=2;B=3;(A+B)");
        assertEquals(expressions, interactiveMode.getAllExpr());
    }
}
