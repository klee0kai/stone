package com.github.klee0kai.stone.wrappers;

import com.github.klee0kai.stone._hidden_.types.StThr;

import java.util.concurrent.ExecutorService;

public class AsyncProvide<T> implements Ref<T> {

    private static final ExecutorService secThread = StThr.singleThreadExecutor("stone_async");

    private T value = null;
    private final Ref<T> call;

    public AsyncProvide(Ref<T> call) {
        this.call = call;
        secThread.submit(() -> {
            get();
        });
    }

    @Override
    public synchronized T get() {
        if (value != null)
            return value;
        return value = call.get();
    }
}
