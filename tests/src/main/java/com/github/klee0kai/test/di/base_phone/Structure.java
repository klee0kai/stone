package com.github.klee0kai.test.di.base_phone;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test.di.base_phone.qualifiers.DataStorageSize;
import com.github.klee0kai.test.di.base_phone.qualifiers.RamSize;
import com.github.klee0kai.test.tech.components.Battery;
import com.github.klee0kai.test.tech.components.DataStorage;
import com.github.klee0kai.test.tech.components.Ram;


@Module
public interface Structure {

    @Provide(cache = Provide.CacheType.Weak)
    Battery battery();


    @Provide(cache = Provide.CacheType.Weak)
    DataStorage dataStorage();

    @Provide(cache = Provide.CacheType.Weak)
    DataStorage dataStorage(DataStorageSize size);

    @Provide(cache = Provide.CacheType.Weak)
    Ram ram();

    @Provide(cache = Provide.CacheType.Weak)
    Ram ram(RamSize ramSize);

}
