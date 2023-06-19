package com.github.klee0kai.test.di.base_phone;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test.di.base_phone.qualifiers.DataStorageSize;
import com.github.klee0kai.test.di.base_phone.qualifiers.PhoneOsType;
import com.github.klee0kai.test.di.base_phone.qualifiers.PhoneOsVersion;
import com.github.klee0kai.test.di.base_phone.qualifiers.RamSize;
import com.github.klee0kai.test.tech.components.Battery;
import com.github.klee0kai.test.tech.components.DataStorage;
import com.github.klee0kai.test.tech.components.OperationSystem;
import com.github.klee0kai.test.tech.components.Ram;


@Module
public interface TechModule {

    @Provide(cache = Provide.CacheType.Weak)
    Battery battery();


    @Provide(cache = Provide.CacheType.Weak)
    DataStorage dataStorage();

    @Provide(cache = Provide.CacheType.Weak)
    DataStorage dataStorage(DataStorageSize size);

    @Provide(cache = Provide.CacheType.Weak)
    Ram ram(RamSize ramSize);


    @Provide(cache = Provide.CacheType.Weak)
    Ram ram();

    @Provide(cache = Provide.CacheType.Weak)
    OperationSystem phoneOs(PhoneOsType osType);

    @Provide(cache = Provide.CacheType.Weak)
    OperationSystem phoneOs(PhoneOsType osType, PhoneOsVersion version);

}
