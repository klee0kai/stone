package com.github.klee0kai.stone.utils;

import com.github.klee0kai.stone.exceptions.ImplementMethodStoneException;

import javax.lang.model.element.Element;
import java.util.LinkedList;

/**
 * We collect all delayed launches and launch at the next stage
 */
public class ImplementMethodCollection {

    private final LinkedList<DelayStart> collectRuns = new LinkedList<>();


    public void execute(String errMessage, Element souseEl, Runnable runnable) {
        collectRuns.add(new DelayStart(errMessage, souseEl, runnable));
    }

    public void execute(Runnable runnable) {
        execute(null, null, runnable);
    }


    public void execute(String errMessage, Runnable runnable) {
        execute(errMessage, null, runnable);
    }


    /**
     * Run all pending tasks
     */
    public void executeAll() {
        for (DelayStart r : collectRuns) {
            try {
                r.run.run();
            } catch (Throwable e) {
                if (r.errorMessage == null) throw e;
                throw new ImplementMethodStoneException(r.errorMessage, e, r.sourceEl);
            }
        }

        collectRuns.clear();
    }

    private static class DelayStart {
        public String errorMessage;
        public Element sourceEl;
        public Runnable run;

        public DelayStart(String errorMessage, Element sourceEl, Runnable run) {
            this.errorMessage = errorMessage;
            this.sourceEl = sourceEl;
            this.run = run;
        }
    }

}
