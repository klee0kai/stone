package com.github.klee0kai.test.tech.phone.base;

import com.github.klee0kai.stone.types.lifecycle.StoneLifeCycleOwner;

public class LifecycleUtils {

    public static StoneLifeCycleOwner createFromATech(ATech phone){
        return listener -> phone.subscribe(
                new ATechLifecycle() {
                    @Override
                    public void onBuy() {

                    }

                    @Override
                    public void onBroken() {

                    }

                    @Override
                    public void onDrown() {
                        listener.protectForInjected(100);
                    }
                }
        );
    }


}
