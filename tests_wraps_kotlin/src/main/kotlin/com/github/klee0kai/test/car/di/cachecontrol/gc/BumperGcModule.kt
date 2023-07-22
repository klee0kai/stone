package com.github.klee0kai.test.car.di.cachecontrol.gc

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test.car.di.cachecontrol.gc.scopes.GcBumperRedScope
import com.github.klee0kai.test.car.di.cachecontrol.gc.scopes.GcBumperScope
import com.github.klee0kai.test.car.model.Bumper
import java.util.*

@Module
open class BumperGcModule {
    @GcBumperScope
    @Provide(cache = Provide.CacheType.Factory)
    open fun bumperFactory(): List<Bumper> {
        return Arrays.asList(Bumper(), Bumper(), Bumper())
    }

    @GcBumperScope
    @Provide(cache = Provide.CacheType.Weak)
    open fun bumperWeak(): List<Bumper> {
        return Arrays.asList(Bumper(), Bumper(), Bumper())
    }

    @GcBumperScope
    @Provide(cache = Provide.CacheType.Soft)
    open fun bumperSoft(): List<Bumper> {
        return Arrays.asList(Bumper(), Bumper(), Bumper())
    }

    @GcBumperScope
    @GcBumperRedScope
    @Provide(cache = Provide.CacheType.Strong)
    open fun bumperStrong(): List<Bumper> {
        return listOf(Bumper(), Bumper(), Bumper())
    }
}
