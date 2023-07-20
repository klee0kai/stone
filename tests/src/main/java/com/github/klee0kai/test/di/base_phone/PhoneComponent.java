package com.github.klee0kai.test.di.base_phone;

import com.github.klee0kai.stone._hidden_.IPrivateComponent;
import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.lifecycle.StoneLifeCycleOwner;
import com.github.klee0kai.test.di.base_phone.qualifiers.DataStorageSize;
import com.github.klee0kai.test.di.base_phone.qualifiers.PhoneOsType;
import com.github.klee0kai.test.di.base_phone.qualifiers.PhoneOsVersion;
import com.github.klee0kai.test.di.base_phone.qualifiers.RamSize;
import com.github.klee0kai.test.tech.components.OperationSystem;
import com.github.klee0kai.test.tech.phone.GoodPhone;
import com.github.klee0kai.test.tech.phone.OnePhone;

@Component(
        qualifiers = {
                DataStorageSize.class,
                RamSize.class,
                PhoneOsType.class,
                OperationSystem.class,
                PhoneOsVersion.class
        }
)
public interface PhoneComponent extends IPrivateComponent {

    TechModule components();

    void inject(OnePhone onePhone);

    void inject(GoodPhone goodPhone, DataStorageSize dataStorageSize, RamSize ramSize);

    void inject(GoodPhone goodPhone, StoneLifeCycleOwner lifeCycleOwner, DataStorageSize dataStorageSize, RamSize ramSize);
}
