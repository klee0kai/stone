package com.github.klee0kai.test.car.di.qualifiers

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test.car.di.qualifiers.qualifiers.*
import com.github.klee0kai.test.car.model.Bumper
import com.github.klee0kai.test.car.model.Car
import com.github.klee0kai.test.car.model.Wheel
import com.github.klee0kai.test.car.model.Window
import java.lang.ref.Reference
import java.lang.ref.WeakReference
import javax.inject.Named
import javax.inject.Provider

@Module
open class CarQCModule {
    @Provide(cache = Provide.CacheType.Factory)
    open fun carSimple(wheel: Wheel?, @BumperQualifier bumper: Bumper?, window: Window?): Car {
        return Car(bumper, wheel, window)
    }

    @Provide(cache = Provide.CacheType.Factory)
    open fun carProvider(
        @BumperQualifier bumper: Provider<Bumper?>,
        wheel: Provider<Wheel?>,
        window: Provider<Window?>
    ): Car {
        return Car(bumper.get(), wheel.get(), window.get())
    }

    @Provide(cache = Provide.CacheType.Factory)
    open fun carRef(
        @BumperQualifier bumper: WeakReference<Bumper?>,
        wheel: Reference<Wheel?>,
        window: Reference<Window?>
    ): Car {
        return Car(bumper.get(), wheel.get(), window.get())
    }

    @Provide(cache = Provide.CacheType.Factory)
    open fun carList(@BumperQualifier bumper: List<Bumper?>?, wheel: List<Wheel?>?, window: List<Window?>?): Car {
        return Car(bumper, wheel, window)
    }

    @Named
    @Provide(cache = Provide.CacheType.Factory)
    open fun carNamedEmpty(@BumperQualifier bumper: List<Bumper?>?, wheel: List<Wheel?>?, window: List<Window?>?): Car {
        val car = Car(bumper, wheel, window)
        car.qualifier = "named_empty"
        return car
    }

    @Named("a")
    @Provide(cache = Provide.CacheType.Factory)
    open fun carNamedA(
        @BumperQualifier(type = BumperQualifier.BumperType.Reinforced) bumper: List<Bumper?>?,
        wheel: List<Wheel?>?,
        window: List<Window?>?
    ): Car {
        val car = Car(bumper, wheel, window)
        car.qualifier = "named_a"
        return car
    }

    @MyQualifier
    @Provide(cache = Provide.CacheType.Factory)
    open fun carMyQualifier(
        @BumperQualifier bumper: List<Bumper?>?,
        wheel: List<Wheel?>?,
        window: List<Window?>?
    ): Car {
        val car = Car(bumper, wheel, window)
        car.qualifier = "my_qualifier"
        return car
    }

    @MyQualifierWithString
    @Provide(cache = Provide.CacheType.Factory)
    open fun carIdQualifier(
        @BumperQualifier bumper: List<Bumper?>?,
        wheel: List<Wheel?>?,
        window: List<Window?>?
    ): Car {
        val car = Car(bumper, wheel, window)
        car.qualifier = "my_qualifier_with_string"
        return car
    }

    @MyQualifierWithString(id = "a")
    @Provide(cache = Provide.CacheType.Factory)
    open fun carIdQualifierA(
        @BumperQualifier(type = BumperQualifier.BumperType.Reinforced) bumper: List<Bumper?>?,
        @WheelCount(count = 4) wheel: List<Wheel?>?,
        window: List<Window?>?
    ): Car {
        val car = Car(bumper, wheel, window)
        car.qualifier = "my_qualifier_a"
        return car
    }

    @MyQualifierWithString(id = "b")
    @Provide(cache = Provide.CacheType.Factory)
    open fun carIdQualifierB(
        @BumperQualifier bumper: List<Bumper?>?,
        wheel: List<Wheel?>?,
        window: List<Window?>?
    ): Car {
        val car = Car(bumper, wheel, window)
        car.qualifier = "my_qualifier_b"
        return car
    }

    @MyQualifierMulti
    @Provide(cache = Provide.CacheType.Factory)
    open fun carQualifierMulti(
        @BumperQualifier bumper: List<Bumper?>?,
        @WheelCount(count = 4) wheel: List<Wheel?>?,
        window: List<Window?>?
    ): Car {
        val car = Car(bumper, wheel, window)
        car.qualifier = "qualifier_multi"
        return car
    }

    @MyQualifierMulti(id = "a", indx = 1)
    @Provide(cache = Provide.CacheType.Factory)
    open fun carQualifierMultiA1(
        @BumperQualifier bumper: List<Bumper?>?,
        wheel: List<Wheel?>?,
        window: List<Window?>?
    ): Car {
        val car = Car(bumper, wheel, window)
        car.qualifier = "qualifier_multi_a1"
        return car
    }

    @MyQualifierMulti(id = "a", indx = 2)
    @Provide(cache = Provide.CacheType.Factory)
    open fun carQualifierMultiA2(
        @BumperQualifier(type = BumperQualifier.BumperType.Reinforced) bumper: List<Bumper?>?,
        @WheelCount(count = 4) wheel: List<Wheel?>?,
        window: List<Window?>?
    ): Car {
        val car = Car(bumper, wheel, window)
        car.qualifier = "qualifier_multi_a2"
        return car
    }

    @MyQualifierMulti(id = "a", indx = 2, type = MyQualifierMulti.Type.HARD)
    @Provide(cache = Provide.CacheType.Factory)
    open fun carQualifierMultiA2Hard(
        @BumperQualifier(type = BumperQualifier.BumperType.Simple) bumper: List<Bumper?>?,
        @WheelCount(count = 4) wheel: List<Wheel?>?,
        window: List<Window?>?
    ): Car {
        val car = Car(bumper, wheel, window)
        car.qualifier = "qualifier_multi_a2_hard"
        return car
    }
}
