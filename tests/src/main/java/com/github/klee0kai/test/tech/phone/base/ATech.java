package com.github.klee0kai.test.tech.phone.base;

import java.util.LinkedList;

public abstract class ATech {

    private LinkedList<ATechLifecycle> listeners = new LinkedList<>();

    public void subscribe(ATechLifecycle listener) {
        this.listeners.add(listener);
    }

    public void onBuy() {
        for (ATechLifecycle lis : listeners)
            lis.onBuy();
    }

    public void onBroken() {
        for (ATechLifecycle lis : listeners)
            lis.onBroken();
    }

    public void onDrown() {
        for (ATechLifecycle lis : listeners)
            lis.onDrown();
    }


}
