package com.github.klee0kai.test.tech.phone;

import com.github.klee0kai.stone.lifecycle.StoneLifeCycleListener;
import com.github.klee0kai.stone.lifecycle.StoneLifeCycleOwner;
import com.github.klee0kai.test.tech.components.Battery;
import com.github.klee0kai.test.tech.components.DataStorage;
import com.github.klee0kai.test.tech.components.Ram;
import com.github.klee0kai.test.tech.phone.base.ATech;
import com.github.klee0kai.test.tech.phone.base.ATechLifecycle;

import javax.inject.Inject;
import javax.inject.Named;

public class OnePhone extends ATech implements StoneLifeCycleOwner {

    @Inject
    public Battery battery;

    @Inject
    @Named("null_args")
    public DataStorage dataStorage;

    @Inject
    @Named("null_args")
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
