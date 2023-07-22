package com.github.klee0kai.test.car.di.wrapped.custom

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test.car.model.Bumper
import com.github.klee0kai.test.car.model.Car
import com.github.klee0kai.test.car.model.Wheel
import com.github.klee0kai.test.car.model.Window
import java.lang.ref.WeakReference

@Module
interface CarCustomWrapperModule {
    @Provide(cache = Provide.CacheType.Factory)
    fun whell(): Wheel?

    @Provide(cache = Provide.CacheType.Factory)
    fun bumper(): WeakReference<Bumper?>?

    @Provide(cache = Provide.CacheType.Factory)
    fun window(): Window?

    @Provide(cache = Provide.CacheType.Factory)
    fun car(bumper: Bumper?, wheel: Wheel?, window: Window?): Car?
}
