package com.github.klee0kai.test.di.base_phone;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.lifecycle.StoneLifeCycleOwner;
import com.github.klee0kai.test.di.base_phone.identifiers.DataStorageSize;
import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsType;
import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsVersion;
import com.github.klee0kai.test.di.base_phone.identifiers.RamSize;
import com.github.klee0kai.test.tech.phone.GoodPhone;
import com.github.klee0kai.test.tech.phone.OnePhone;

@Component(
        identifiers = {
                DataStorageSize.class,
                RamSize.class,
                PhoneOsType.class,
                PhoneOsVersion.class
        }
)
public interface PhoneComponent {

    TechModule components();

    void inject(OnePhone onePhone);

    void inject(GoodPhone goodPhone, DataStorageSize dataStorageSize, RamSize ramSize);

    void inject(GoodPhone goodPhone, StoneLifeCycleOwner lifeCycleOwner, DataStorageSize dataStorageSize, RamSize ramSize);
}
