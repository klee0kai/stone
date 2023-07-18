package com.github.klee0kai.test.di.techfactory;

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

import javax.inject.Named;


@Module
public interface TechFactoryModule {

    @Provide(cache = Provide.CacheType.Factory)
    Battery battery();

    @Named("null_args")
    @Provide(cache = Provide.CacheType.Factory)
    DataStorage dataStorage();

    @Provide(cache = Provide.CacheType.Factory)
    DataStorage dataStorage(DataStorageSize size);


    @Named("null_args")
    @Provide(cache = Provide.CacheType.Factory)
    Ram ram();

    @Provide(cache = Provide.CacheType.Factory)
    Ram ram(RamSize ramSize);


    @Named("null_args")
    @Provide(cache = Provide.CacheType.Factory)
    OperationSystem phoneOs(PhoneOsType osType);

    @Provide(cache = Provide.CacheType.Factory)
    OperationSystem phoneOs(PhoneOsType phoneOsType, PhoneOsVersion version);

}
