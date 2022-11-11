package com.github.klee0kai.test.lifecycle.stone_util;

import com.github.klee0kai.stone.types.lifecycle.IStoneLifeCycleOwner;
import com.github.klee0kai.test.lifecycle.base.APhone;
import com.github.klee0kai.test.lifecycle.base.APhoneLifecycle;

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
