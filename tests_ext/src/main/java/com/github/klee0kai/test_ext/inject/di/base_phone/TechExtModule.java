package com.github.klee0kai.test_ext.inject.di.base_phone;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test.di.base_phone.TechModule;
import com.github.klee0kai.test.di.base_phone.qualifiers.DataStorageSize;
import com.github.klee0kai.test.di.base_phone.qualifiers.PhoneOsType;
import com.github.klee0kai.test.di.base_phone.qualifiers.PhoneOsVersion;
import com.github.klee0kai.test.di.base_phone.qualifiers.RamSize;
import com.github.klee0kai.test.tech.components.Battery;
import com.github.klee0kai.test.tech.components.DataStorage;
import com.github.klee0kai.test.tech.components.OperationSystem;
import com.github.klee0kai.test.tech.components.Ram;
import com.github.klee0kai.test_ext.inject.tech.components.DDR3Ram;


@Module
public interface TechExtModule extends TechModule {

    @Override
    @Provide(cache = Provide.CacheType.Weak)
    DDR3Ram ram();

    @Override
    @Provide(cache = Provide.CacheType.Weak)
    DDR3Ram ram(RamSize ramSize);


}
