package com.github.klee0kai.test.car.di.bestOf.blue

import com.github.klee0kai.stone.annotations.component.*
import com.github.klee0kai.test.car.di.bestOf.both.BothCarModule
import com.github.klee0kai.test.car.model.Car
import javax.inject.Named
import javax.inject.Provider

@Component
interface BlueCarComponent {
    fun myModule(): BothCarModule

    @Named("blueCar")
    fun blueCar(): Provider<Car>
}
