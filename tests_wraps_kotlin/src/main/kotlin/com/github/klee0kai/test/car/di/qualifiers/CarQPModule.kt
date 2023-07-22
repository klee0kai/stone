package com.github.klee0kai.test.car.di.qualifiers

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.stone.wrappers.Ref
import com.github.klee0kai.test.car.di.qualifiers.qualifiers.BumperQualifier
import com.github.klee0kai.test.car.di.qualifiers.qualifiers.WheelCount
import com.github.klee0kai.test.car.model.Bumper
import com.github.klee0kai.test.car.model.Wheel
import com.github.klee0kai.test.car.model.Window
import java.util.*

@Module
abstract class CarQPModule {
    @Provide(cache = Provide.CacheType.Factory)
    abstract fun wheel(): Wheel?

    @WheelCount(count = 4)
    @Provide(cache = Provide.CacheType.Factory)
    open fun fourWheel(): List<Wheel> {
        return Arrays.asList(Wheel(), Wheel(), Wheel(), Wheel())
    }

    @BumperQualifier(type = BumperQualifier.BumperType.Simple)
    @Provide(cache = Provide.CacheType.Factory)
    open fun bumperSimple(): Bumper {
        val bumper = Bumper()
        bumper.qualifier = "simple"
        return bumper
    }

    @BumperQualifier(type = BumperQualifier.BumperType.Reinforced)
    @Provide(cache = Provide.CacheType.Factory)
    open fun bumper(): Bumper {
        val bumper = Bumper()
        bumper.qualifier = "reinforced"
        return bumper
    }

    @Provide(cache = Provide.CacheType.Factory)
    abstract fun windows(): Ref<List<Window?>?>?
}
