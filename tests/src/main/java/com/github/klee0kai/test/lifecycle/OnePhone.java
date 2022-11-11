package com.github.klee0kai.test.lifecycle;

import com.github.klee0kai.stone.annotations.component.Inject;
import com.github.klee0kai.stone.types.lifecycle.IStoneLifeCycleListener;
import com.github.klee0kai.stone.types.lifecycle.IStoneLifeCycleOwner;
import com.github.klee0kai.test.lifecycle.base.APhone;
import com.github.klee0kai.test.lifecycle.base.APhoneLifecycle;
import com.github.klee0kai.test.lifecycle.structure.Battery;
import com.github.klee0kai.test.lifecycle.structure.DataStorage;
import com.github.klee0kai.test.lifecycle.structure.Ram;

public class OnePhone extends APhone implements IStoneLifeCycleOwner {

    @Inject
    public Battery battery;
    @Inject
    public DataStorage dataStorage;
    @Inject
    public Ram ram;


    @Override
    public void subscribe(IStoneLifeCycleListener listener) {
        super.subscribe(new APhoneLifecycle() {
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
        });
    }

    public void buy() {
        onBuy();
        PhoneStore.DI.inject(this);
    }


    public void drown() {
        onDrown();
        battery = null;
        dataStorage = null;
        ram = null;
    }

    public void broke() {
        onBroken();
        battery = null;
        dataStorage = null;
        ram = null;
    }

    public void repair(){
        PhoneStore.DI.inject(this);
    }

}
