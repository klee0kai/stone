package com.github.klee0kai.stone.closed.types;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Stone Private class
 */
public class DataAwait<T> {

    private final LinkedList<T> values = new LinkedList<>();
    private final AtomicInteger awaitCount = new AtomicInteger(0);

    public synchronized T await() {
        try {
            if (!values.isEmpty())
                return values.poll();

            awaitCount.incrementAndGet();
            wait();
            return values.poll();
        } catch (InterruptedException e) {
            return null;
        } finally {
            awaitCount.decrementAndGet();
        }
    }


    public synchronized T await(long millis) {
        try {
            if (!values.isEmpty())
                return values.poll();

            awaitCount.incrementAndGet();
            wait(millis);
            return values.poll();
        } catch (InterruptedException e) {
            return null;
        } finally {
            awaitCount.decrementAndGet();
        }
    }

    public synchronized boolean trySend(T value) {
        this.values.add(value);
        if (awaitCount.get() > 0) {
            notifyAll();
            return true;
        }
        return false;
    }

    public synchronized void clearAll() {
        values.clear();
    }

}
