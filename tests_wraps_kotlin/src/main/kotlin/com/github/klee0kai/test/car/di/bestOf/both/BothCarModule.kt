package com.github.klee0kai.test.car.di.bestOf.both

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.stone.wrappers.Ref
import com.github.klee0kai.test.car.model.Bumper
import com.github.klee0kai.test.car.model.Car
import com.github.klee0kai.test.car.model.Wheel
import com.github.klee0kai.test.car.model.Window
import java.lang.ref.WeakReference
import java.util.*
import javax.inject.Named
import javax.inject.Provider

@Module
abstract class BothCarModule {
    @Provide(cache = Provide.CacheType.Weak)
    open fun fourWheels(): List<Wheel> {
        return Arrays.asList(Wheel(), Wheel(), Wheel(), Wheel())
    }

    @Named
    @Provide(cache = Provide.CacheType.Weak)
    abstract fun spareWheel(): Wheel

    @Provide(cache = Provide.CacheType.Factory)
    abstract fun frontWindow(): Provider<Window>

    @Named
    @Provide(cache = Provide.CacheType.Factory)
    abstract fun backWindow(): Provider<WeakReference<Window>>

    @Named
    @Provide(cache = Provide.CacheType.Factory)
    open fun passengerWindows(): List<Ref<Window>> {
        return listOf(Ref<Window> { Window() }, Ref<Window> { Window() })
    }

    @Provide(cache = Provide.CacheType.Weak)
    open fun bumpers(): Collection<Bumper> {
        return listOf(Bumper(), Bumper())
    }

    @Named("redCar")
    @Provide(cache = Provide.CacheType.Weak)
    abstract fun redCar(bumper: Bumper, wheel: Wheel, window: Window): List<Car>

    @Named("blueCar")
    @Provide(cache = Provide.CacheType.Weak)
    abstract fun blueCar(bumpers: List<Bumper>, wheels: List<Wheel>, windows: List<Window>): Car
}
