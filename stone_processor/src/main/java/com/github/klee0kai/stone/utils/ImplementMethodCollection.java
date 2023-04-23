package com.github.klee0kai.stone.utils;

import com.github.klee0kai.stone.exceptions.ImplementMethodStoneException;
import com.github.klee0kai.stone.model.Pair;

import java.util.LinkedList;

public class ImplementMethodCollection {

    private final LinkedList<Pair<String, Runnable>> collectRuns = new LinkedList<>();

    public void execute(String errMessage, Runnable runnable) {
        collectRuns.add(new Pair<>(errMessage, runnable));
    }

    public void executeAll() {
        for (Pair<String, Runnable> r : collectRuns) {
            try {
                r.second.run();
            } catch (Throwable e) {
                if (r.first == null) throw new ImplementMethodStoneException(e);
                throw new ImplementMethodStoneException(r.first, e);
            }
        }

        collectRuns.clear();
    }

}
