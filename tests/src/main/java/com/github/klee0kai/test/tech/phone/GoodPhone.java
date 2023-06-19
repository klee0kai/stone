package com.github.klee0kai.test.tech.phone;

import com.github.klee0kai.stone.types.lifecycle.StoneLifeCycleOwner;
import com.github.klee0kai.test.tech.components.Battery;
import com.github.klee0kai.test.tech.components.DataStorage;
import com.github.klee0kai.test.tech.components.OperationSystem;
import com.github.klee0kai.test.tech.components.Ram;
import com.github.klee0kai.test.tech.phone.base.ATech;
import com.github.klee0kai.test.tech.phone.base.LifecycleUtils;

import javax.inject.Inject;

public class GoodPhone extends ATech {

    public StoneLifeCycleOwner lifeCycleOwner = LifecycleUtils.createFromATech(this);


    @Inject
    public Battery battery;
    @Inject
    public DataStorage dataStorage;
    @Inject
    public Ram ram;

    @Inject
    public OperationSystem os;

    public void dropToWater() {
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
