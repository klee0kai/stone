package com.github.klee0kai.test.boxed.di.inject

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.stone.wrappers.Ref
import com.github.klee0kai.test.boxed.model.CarBox
import com.github.klee0kai.test.car.model.Bumper
import com.github.klee0kai.test.car.model.Wheel
import com.github.klee0kai.test.car.model.Window
import java.lang.ref.WeakReference
import java.util.*
import javax.inject.Named
import javax.inject.Provider

@Module
open class CarBoxedInjectModule {
    @Named("fourWheels")
    @Provide(cache = Provide.CacheType.Weak)
    open fun fourWheels(): List<CarBox<Wheel>> {
        return listOf(
            CarBox(Wheel()),
            CarBox(Wheel()),
            CarBox(Wheel()),
            CarBox(Wheel()),
        )
    }

    @Provide(cache = Provide.CacheType.Weak)
    open fun spareWheel(): CarBox<Wheel> {
        return CarBox(Wheel())
    }

    @Provide(cache = Provide.CacheType.Factory)
    open fun frontWindow(): Provider<CarBox<Window>> {
        return Provider { CarBox(Window()) }
    }

    @Named
    @Provide(cache = Provide.CacheType.Factory)
    open fun backWindow(): Provider<WeakReference<CarBox<Window>>> {
        return Provider { WeakReference(CarBox(Window())) }
    }

    @Named
    @Provide(cache = Provide.CacheType.Factory)
    open fun passengerWindows(): List<Ref<CarBox<Window>>> {
        return listOf(Ref { CarBox(Window()) }, Ref { CarBox(Window()) })
    }

    @Provide(cache = Provide.CacheType.Weak)
    open fun bumpers(): Collection<CarBox<Bumper>> {
        return listOf(CarBox(Bumper()), CarBox(Bumper()))
    }
}
