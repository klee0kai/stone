package com.github.klee0kai.stone._hidden_.types;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Stone Private class
 */
public class StThr {

    public static ThreadPoolExecutor singleThreadExecutor(String name) {
        return new ThreadPoolExecutor(0, 1, 0,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(), runnable -> {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            thread.setName(name);
            return thread;
        });
    }

}
