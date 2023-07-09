package com.github.klee0kai.test.car.di.cachecontrol.gc;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test.car.di.cachecontrol.gc.scopes.GcBumperRedScope;
import com.github.klee0kai.test.car.di.cachecontrol.gc.scopes.GcBumperScope;
import com.github.klee0kai.test.car.model.Bumper;

import java.util.Arrays;
import java.util.List;

@Module
public class BumperGcModule {

    @GcBumperScope
    @Provide(cache = Provide.CacheType.Factory)
    public List<Bumper> bumperFactory() {
        return Arrays.asList(new Bumper(), new Bumper(), new Bumper());
    }


    @GcBumperScope
    @Provide(cache = Provide.CacheType.Weak)
    public List<Bumper> bumperWeak() {
        return Arrays.asList(new Bumper(), new Bumper(), new Bumper());
    }


    @GcBumperScope
    @Provide(cache = Provide.CacheType.Soft)
    public List<Bumper> bumperSoft() {
        return Arrays.asList(new Bumper(), new Bumper(), new Bumper());
    }

    @GcBumperScope
    @GcBumperRedScope
    @Provide(cache = Provide.CacheType.Strong)
    public List<Bumper> bumperStrong() {
        return Arrays.asList(new Bumper(), new Bumper(), new Bumper());
    }


}
