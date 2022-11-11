package com.github.klee0kai.test.lifecycle.di;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.types.lifecycle.IStoneLifeCycleOwner;
import com.github.klee0kai.test.lifecycle.GoodPhone;
import com.github.klee0kai.test.lifecycle.OnePhone;
import com.github.klee0kai.test.lifecycle.di.qualifiers.DataStorageSize;
import com.github.klee0kai.test.lifecycle.di.qualifiers.RamSize;

@Component(
        qualifiers = {DataStorageSize.class, RamSize.class}
)
public interface PComponent {

    Structure structure();

    void inject(OnePhone onePhone);

    void inject(GoodPhone goodPhone, DataStorageSize dataStorageSize, RamSize ramSize);

    void inject(GoodPhone goodPhone, IStoneLifeCycleOwner lifeCycleOwner, DataStorageSize dataStorageSize, RamSize ramSize);
}
