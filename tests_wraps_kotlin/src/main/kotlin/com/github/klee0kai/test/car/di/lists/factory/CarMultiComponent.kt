package com.github.klee0kai.test.car.di.lists.factory

import com.github.klee0kai.stone.annotations.component.*
import com.github.klee0kai.stone.wrappers.Ref
import com.github.klee0kai.test.car.model.Bumper
import com.github.klee0kai.test.car.model.Car
import com.github.klee0kai.test.car.model.Wheel
import com.github.klee0kai.test.car.model.Window
import java.lang.ref.WeakReference
import javax.inject.Named
import javax.inject.Provider

@Component
interface CarMultiComponent {
    fun module(): CarMultiModule?
    fun singleBumper(): Ref<Bumper?>?
    fun wheels(): List<Provider<WeakReference<Wheel?>?>?>?
    fun wheel(): Wheel?
    fun windows(): List<List<Window?>?>?
    fun windowsProviding(): List<Provider<List<Window?>?>?>?
    fun cars(): List<Car?>?

    @Named("blueCar")
    fun blueCar(): Provider<Car?>?

    @Named("redCar")
    fun redCar(): WeakReference<Car?>?
}
