package com.github.klee0kai.stone.closed.types;

import java.util.LinkedList;

/**
 * Stone Private class
 */
public class DataAwait<T> {

    private final LinkedList<T> values = new LinkedList<>();
    private int awaitCount = 0;

    public synchronized T await() {
        if (!values.isEmpty())
            return values.poll();

        try {
            awaitCount++;
            wait();
        } catch (InterruptedException ignore) {
        } finally {
            awaitCount--;
        }

        return values.poll();
    }


    public synchronized T await(long millis) {
        if (!values.isEmpty())
            return values.poll();

        try {
            awaitCount++;
            wait(millis);
        } catch (InterruptedException ignore) {
        } finally {
            awaitCount--;
        }

        return values.poll();
    }

    public synchronized boolean trySend(T value) {
        this.values.add(value);
        if (awaitCount > 0) {
            notifyAll();
            return true;
        }
        return false;
    }

    public synchronized void clearAll() {
        values.clear();
    }

}
