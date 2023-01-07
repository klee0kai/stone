package com.github.klee0kai.test.tech.phone;

import com.github.klee0kai.stone.types.lifecycle.IStoneLifeCycleListener;
import com.github.klee0kai.stone.types.lifecycle.IStoneLifeCycleOwner;
import com.github.klee0kai.test.tech.PhoneStore;
import com.github.klee0kai.test.tech.phone.base.APhone;
import com.github.klee0kai.test.tech.phone.base.APhoneLifecycle;
import com.github.klee0kai.test.tech.components.Battery;
import com.github.klee0kai.test.tech.components.DataStorage;
import com.github.klee0kai.test.tech.components.Ram;

import javax.inject.Inject;

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


    public void dropToWatter() {
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
