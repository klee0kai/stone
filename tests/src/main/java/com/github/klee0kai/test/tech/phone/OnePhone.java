package com.github.klee0kai.test.tech.phone;

import com.github.klee0kai.stone.types.lifecycle.StoneLifeCycleListener;
import com.github.klee0kai.stone.types.lifecycle.StoneLifeCycleOwner;
import com.github.klee0kai.test.tech.components.Battery;
import com.github.klee0kai.test.tech.components.DataStorage;
import com.github.klee0kai.test.tech.components.Ram;
import com.github.klee0kai.test.tech.phone.base.ATech;
import com.github.klee0kai.test.tech.phone.base.ATechLifecycle;

import javax.inject.Inject;

public class OnePhone extends ATech implements StoneLifeCycleOwner {

    @Inject
    public Battery battery;
    @Inject
    public DataStorage dataStorage;
    @Inject
    public Ram ram;


    @Override
    public void subscribe(StoneLifeCycleListener listener) {
        super.subscribe(new ATechLifecycle() {
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

}
