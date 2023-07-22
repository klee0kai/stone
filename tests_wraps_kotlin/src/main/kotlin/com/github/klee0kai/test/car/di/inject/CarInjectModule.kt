package com.github.klee0kai.test.car.di.inject

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.stone.wrappers.Ref
import com.github.klee0kai.test.car.model.Bumper
import com.github.klee0kai.test.car.model.Wheel
import com.github.klee0kai.test.car.model.Window
import java.lang.ref.WeakReference
import javax.inject.Named
import javax.inject.Provider

@Module
abstract class CarInjectModule {
    @Provide(cache = Provide.CacheType.Weak)
    open fun fourWheels(): List<Wheel> {
        return listOf(Wheel(), Wheel(), Wheel(), Wheel())
    }

    @Named
    @Provide(cache = Provide.CacheType.Weak)
    abstract fun spareWheel(): Wheel?

    @Provide(cache = Provide.CacheType.Factory)
    abstract fun frontWindow(): Provider<Window?>?

    @Named
    @Provide(cache = Provide.CacheType.Factory)
    abstract fun backWindow(): Provider<WeakReference<Window?>?>?

    @Named
    @Provide(cache = Provide.CacheType.Factory)
    open fun passengerWindows(): List<Ref<Window>> {
        return listOf(Ref<Window> { Window() }, Ref<Window> { Window() })
    }

    @Provide(cache = Provide.CacheType.Weak)
    open fun bumpers(): Collection<Bumper> {
        return listOf(Bumper(), Bumper())
    }
}
