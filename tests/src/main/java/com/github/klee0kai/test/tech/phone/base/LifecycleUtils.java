package com.github.klee0kai.test.tech.phone.base;

import com.github.klee0kai.stone.types.lifecycle.IStoneLifeCycleOwner;

public class LifecycleUtils {

    public static IStoneLifeCycleOwner createFromPhone(APhone phone){
        return listener -> phone.subscribe(
                new APhoneLifecycle() {
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
