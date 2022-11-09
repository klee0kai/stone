package com.github.klee0kai.test.inject.di;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test.inject.forest.Blood;
import com.github.klee0kai.test.inject.forest.Earth;

import java.awt.*;


@Module
public abstract class UnitedModule {

    @Provide(cache = Provide.CacheType.STRONG)
    public Blood blood() {
        return new Blood(Color.RED);
    }

    @Provide(cache = Provide.CacheType.SOFT)
    public abstract Earth earth();


}
