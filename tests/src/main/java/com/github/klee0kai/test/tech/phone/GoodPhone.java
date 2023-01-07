package com.github.klee0kai.test.tech.phone;

import com.github.klee0kai.stone.types.lifecycle.IStoneLifeCycleOwner;
import com.github.klee0kai.test.lifecycle.PhoneStore;
import com.github.klee0kai.test.tech.phone.base.APhone;
import com.github.klee0kai.test.lifecycle.di.qualifiers.DataStorageSize;
import com.github.klee0kai.test.lifecycle.di.qualifiers.RamSize;
import com.github.klee0kai.test.tech.phone.base.LifecycleUtils;
import com.github.klee0kai.test.tech.components.Battery;
import com.github.klee0kai.test.tech.components.DataStorage;
import com.github.klee0kai.test.tech.components.Ram;

import javax.inject.Inject;

public class GoodPhone extends APhone {

    public IStoneLifeCycleOwner lifeCycleOwner = LifecycleUtils.createFromPhone(this);

    private final DataStorageSize dataStorageSize;
    private final RamSize ramSize;


    @Inject
    public Battery battery;
    @Inject
    public DataStorage dataStorage;
    @Inject
    public Ram ram;

    public GoodPhone(DataStorageSize dataStorageSize, RamSize ramSize) {
        this.dataStorageSize = dataStorageSize;
        this.ramSize = ramSize;
    }

    public void buy() {
        onBuy();
        PhoneStore.DI.inject(this, lifeCycleOwner, dataStorageSize, ramSize);
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

    public void repair() {
        PhoneStore.DI.inject(this, dataStorageSize, ramSize);
    }

}
