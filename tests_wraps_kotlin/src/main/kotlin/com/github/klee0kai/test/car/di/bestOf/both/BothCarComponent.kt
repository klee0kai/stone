package com.github.klee0kai.test.car.di.bestOf.both

import com.github.klee0kai.stone.annotations.component.*
import com.github.klee0kai.test.car.model.Car
import java.lang.ref.WeakReference
import javax.inject.Named
import javax.inject.Provider

@Component
interface BothCarComponent {
    fun myModule(): BothCarModule?

    @Named("blueCar")
    fun blueCar(): Provider<Car>

    @Named("redCar")
    fun redCar(): WeakReference<Car>
}
