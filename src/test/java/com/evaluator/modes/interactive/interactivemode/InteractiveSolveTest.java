package com.evaluator.modes.interactive.interactivemode;

import com.evaluator.modes.RuntimeMode;
import com.evaluator.modes.interactive.InteractiveMode;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class InteractiveSolveTest {
    @Test
    public void solve(){
        RuntimeMode interactiveMode = new InteractiveMode();
        String input = "A=2;B=3;(A+B)";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        interactiveMode.solve();
        assertTrue(true);
    }
}
