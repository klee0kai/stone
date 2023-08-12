package com.github.klee0kai.stone.wrappers;

import com.github.klee0kai.stone._hidden_.types.Threads;

import java.util.concurrent.ExecutorService;

/**
 * Provision of an object with a provider on the 2nd stream.
 * By the time of use, the object will already be created on the second thread,
 * or lazy initialization will be performed.
 * <pre>{@code
 *     @Component
 *     interface Component {
 *
 *         AsyncProvide<WelcomePresenter> presenter();
 *
 *     }
 * }</pre>
 */
public class AsyncProvide<T> implements Ref<T> {

    private static final ExecutorService secThread = Threads.singleThreadExecutor("stone_async");

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
