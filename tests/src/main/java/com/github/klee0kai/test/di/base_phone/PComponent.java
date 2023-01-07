package com.github.klee0kai.test.di.base_phone;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.types.lifecycle.IStoneLifeCycleOwner;
import com.github.klee0kai.test.di.base_phone.qualifiers.DataStorageSize;
import com.github.klee0kai.test.di.base_phone.qualifiers.RamSize;
import com.github.klee0kai.test.tech.phone.GoodPhone;
import com.github.klee0kai.test.tech.phone.OnePhone;

@Component(
        qualifiers = {DataStorageSize.class, RamSize.class}
)
public interface PComponent {

    Structure structure();

    void inject(OnePhone onePhone);

    void inject(GoodPhone goodPhone, DataStorageSize dataStorageSize, RamSize ramSize);

    void inject(GoodPhone goodPhone, IStoneLifeCycleOwner lifeCycleOwner, DataStorageSize dataStorageSize, RamSize ramSize);
}
