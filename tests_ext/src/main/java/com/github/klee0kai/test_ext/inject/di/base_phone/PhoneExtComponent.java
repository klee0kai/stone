package com.github.klee0kai.test_ext.inject.di.base_phone;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.component.ExtendOf;
import com.github.klee0kai.stone.lifecycle.StoneLifeCycleOwner;
import com.github.klee0kai.test.di.base_phone.PhoneComponent;
import com.github.klee0kai.test.di.base_phone.identifiers.DataStorageSize;
import com.github.klee0kai.test.di.base_phone.identifiers.RamSize;
import com.github.klee0kai.test.tech.phone.GoodPhone;
import com.github.klee0kai.test.tech.phone.OnePhone;

@Component
public interface PhoneExtComponent extends PhoneComponent {

    TechExtModule components();

    @ExtendOf
    void extOf(PhoneComponent parent);

    void injectExt(OnePhone onePhone);

    void injectExt(GoodPhone goodPhone, DataStorageSize dataStorageSize, RamSize ramSize);

    void injectExt(GoodPhone goodPhone, StoneLifeCycleOwner lifeCycleOwner, DataStorageSize dataStorageSize, RamSize ramSize);
}
