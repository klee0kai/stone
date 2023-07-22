package com.github.klee0kai.test.car.di.bestOf.red

import com.github.klee0kai.stone.annotations.component.*
import com.github.klee0kai.test.car.di.bestOf.both.BothCarModule
import com.github.klee0kai.test.car.model.Car
import java.lang.ref.WeakReference
import javax.inject.Named

@Component
interface RedCarComponent {
    fun myModule(): BothCarModule

    @Named("redCar")
    fun redCar(): WeakReference<Car?>
}
