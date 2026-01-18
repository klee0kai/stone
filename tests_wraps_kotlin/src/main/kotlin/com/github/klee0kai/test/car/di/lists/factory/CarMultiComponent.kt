package com.github.klee0kai.test.car.di.lists.factory

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.annotations.qualifier.IgnoreQualifier
import com.github.klee0kai.stone.weakref.Ref
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

    @IgnoreQualifier
    fun wheels(): List<Provider<WeakReference<Wheel?>?>?>?

    fun wheel(): Wheel?

    @IgnoreQualifier
    fun windows(): List<List<Window?>?>?

    @IgnoreQualifier
    fun windowsProviding(): List<Provider<List<Window>?>?>?

    @IgnoreQualifier
    fun cars(): List<Car?>?

    @Named("blueCar")
    fun blueCar(): Provider<Car?>?

    @Named("redCar")
    fun redCar(): WeakReference<Car?>?
}
