package com.github.klee0kai.test.inject.di;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test.inject.forest.Blood;
import com.github.klee0kai.test.inject.forest.Earth;
import com.github.klee0kai.test.inject.forest.History;

import java.awt.*;


@Module
public abstract class UnitedModule {

    @Provide(cache = Provide.CacheType.Strong)
    public Blood blood() {
        return new Blood(Color.RED);
    }

    @Provide(cache = Provide.CacheType.Soft)
    public abstract Earth earth();

    @Provide(cache = Provide.CacheType.Weak)
    public abstract History history();


}
