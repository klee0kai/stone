package com.github.klee0kai.test_ext.inject.di.base_phone;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.types.lifecycle.IStoneLifeCycleOwner;
import com.github.klee0kai.test.di.base_phone.PhoneComponent;
import com.github.klee0kai.test.di.base_phone.TechModule;
import com.github.klee0kai.test.di.base_phone.qualifiers.DataStorageSize;
import com.github.klee0kai.test.di.base_phone.qualifiers.RamSize;
import com.github.klee0kai.test.tech.phone.GoodPhone;
import com.github.klee0kai.test.tech.phone.OnePhone;

@Component
public interface PhoneExtComponent extends PhoneComponent {

    TechExtModule components();

    void injectExt(OnePhone onePhone);

    void injectExt(GoodPhone goodPhone, DataStorageSize dataStorageSize, RamSize ramSize);

    void injectExt(GoodPhone goodPhone, IStoneLifeCycleOwner lifeCycleOwner, DataStorageSize dataStorageSize, RamSize ramSize);
}
