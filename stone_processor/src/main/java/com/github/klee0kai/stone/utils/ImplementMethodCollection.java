package com.github.klee0kai.stone.utils;

import com.github.klee0kai.stone.exceptions.ImplementMethodStoneException;
import com.github.klee0kai.stone.model.Pair;

import java.util.LinkedList;

/**
 * We collect all delayed launches and launch at the next stage
 */
public class ImplementMethodCollection {

    private final LinkedList<Pair<String, Runnable>> collectRuns = new LinkedList<>();

    /**
     * Run on next stage.
     *
     * @param errMessage error message after exception
     * @param runnable   delayed runnable
     */
    public void execute(String errMessage, Runnable runnable) {
        collectRuns.add(new Pair<>(errMessage, runnable));
    }

    /**
     * Run all pending tasks
     */
    public void executeAll() {
        for (Pair<String, Runnable> r : collectRuns) {
            try {
                r.second.run();
            } catch (Throwable e) {
                if (r.first == null) throw e;
                throw new ImplementMethodStoneException(r.first, e);
            }
        }

        collectRuns.clear();
    }

}
