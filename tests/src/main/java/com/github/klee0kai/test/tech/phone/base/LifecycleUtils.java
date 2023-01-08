package com.github.klee0kai.test.tech.phone.base;

import com.github.klee0kai.stone.types.lifecycle.IStoneLifeCycleOwner;

public class LifecycleUtils {

    public static IStoneLifeCycleOwner createFromATech(ATech phone){
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
