package com.github.klee0kai.test.di.base_forest;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test.mowgli.body.Blood;

import java.awt.*;


@Module
public abstract class UnitedBlueModule extends UnitedModule {

    @Provide(cache = Provide.CacheType.Strong)
    public Blood blood() {
        return new Blood(Color.BLUE);
    }

}
