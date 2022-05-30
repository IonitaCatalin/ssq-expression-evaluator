package com.evaluator.modes;

import java.util.List;

/**
 * @author Popa Stefan
 * @since 01.05.2022
 */
public interface RuntimeMode {
    List<String> getAllExpr();
    void solve();
}
