package com.github.klee0kai.test.lifecycle.base;

import java.util.LinkedList;

public abstract class APhone {

    private LinkedList<APhoneLifecycle> listeners = new LinkedList<>();

    public void subscribe(APhoneLifecycle listener) {
        this.listeners.add(listener);
    }

    public void onBuy() {
        for (APhoneLifecycle lis : listeners)
            lis.onBuy();
    }

    public void onBroken() {
        for (APhoneLifecycle lis : listeners)
            lis.onBroken();
    }

    public void onDrown() {
        for (APhoneLifecycle lis : listeners)
            lis.onDrown();
    }


}
