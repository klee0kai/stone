package com.github.klee0kai.test.di.base_phone;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test.di.base_phone.identifiers.DataStorageSize;
import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsType;
import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsVersion;
import com.github.klee0kai.test.di.base_phone.identifiers.RamSize;
import com.github.klee0kai.test.tech.components.Battery;
import com.github.klee0kai.test.tech.components.DataStorage;
import com.github.klee0kai.test.tech.components.OperationSystem;
import com.github.klee0kai.test.tech.components.Ram;

import javax.inject.Named;


@Module
public interface TechModule {

    @Provide(cache = Provide.CacheType.Weak)
    Battery battery();

    @Named("null_args")
    @Provide(cache = Provide.CacheType.Weak)
    DataStorage dataStorage();

    @Provide(cache = Provide.CacheType.Weak)
    DataStorage dataStorage(DataStorageSize size);

    @Provide(cache = Provide.CacheType.Weak)
    Ram ram(RamSize ramSize);


    @Named("null_args")
    @Provide(cache = Provide.CacheType.Weak)
    Ram ram();

    @Named("null_args")
    @Provide(cache = Provide.CacheType.Weak)
    OperationSystem phoneOs(PhoneOsType osType);

    @Provide(cache = Provide.CacheType.Weak)
    OperationSystem phoneOs(PhoneOsType osType, PhoneOsVersion version);

}
