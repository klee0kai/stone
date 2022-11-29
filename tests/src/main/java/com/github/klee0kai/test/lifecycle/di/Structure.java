package com.github.klee0kai.test.lifecycle.di;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test.lifecycle.di.qualifiers.DataStorageSize;
import com.github.klee0kai.test.lifecycle.di.qualifiers.RamSize;
import com.github.klee0kai.test.lifecycle.structure.Battery;
import com.github.klee0kai.test.lifecycle.structure.DataStorage;
import com.github.klee0kai.test.lifecycle.structure.Ram;


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
