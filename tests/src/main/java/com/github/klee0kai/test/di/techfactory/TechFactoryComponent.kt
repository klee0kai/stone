package com.github.klee0kai.test.di.techfactory;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.test.di.base_phone.identifiers.DataStorageSize;
import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsType;
import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsVersion;
import com.github.klee0kai.test.di.base_phone.identifiers.RamSize;

@Component(
        identifiers = {
                RamSize.class, DataStorageSize.class,
                PhoneOsType.class, PhoneOsVersion.class
        }
)
public interface TechFactoryComponent extends ITechProviderComponent {

    TechFactoryModule factory();


}
