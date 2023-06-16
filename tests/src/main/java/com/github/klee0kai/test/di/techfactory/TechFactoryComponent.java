package com.github.klee0kai.test.di.techfactory;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.closed.IComponent;
import com.github.klee0kai.test.di.base_phone.qualifiers.DataStorageSize;
import com.github.klee0kai.test.di.base_phone.qualifiers.PhoneOsType;
import com.github.klee0kai.test.di.base_phone.qualifiers.PhoneOsVersion;
import com.github.klee0kai.test.di.base_phone.qualifiers.RamSize;

@Component(
        qualifiers = {
                RamSize.class, DataStorageSize.class,
                PhoneOsType.class, PhoneOsVersion.class
        }
)
public interface TechFactoryComponent extends ITechProviderComponent, IComponent {

    TechFactoryModule factory();


}
