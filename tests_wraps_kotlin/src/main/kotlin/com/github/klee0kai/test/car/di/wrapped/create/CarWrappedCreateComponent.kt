package com.github.klee0kai.test.car.di.wrapped.create

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.wrappers.AsyncLazy
import com.github.klee0kai.stone.wrappers.FantomAsyncProvide
import com.github.klee0kai.stone.wrappers.LazyProvide
import com.github.klee0kai.test.car.model.Bumper
import com.github.klee0kai.test.car.model.Car
import com.github.klee0kai.test.car.model.Wheel
import com.github.klee0kai.test.car.model.Window
import java.lang.ref.WeakReference
import javax.inject.Provider

@Component
interface CarWrappedCreateComponent {
    fun factory(): CarWrappedCreateModule?

    fun wheel(): Wheel?

    fun wheelProvide(): Provider<Wheel?>?

    fun wheelLazy(): LazyProvide<Wheel?>?

    fun wheelWeak(): WeakReference<Wheel?>?

    fun whellProviderWeak(): Provider<WeakReference<Wheel?>?>?

    fun whellLazyProviderWeak(): LazyProvide<Provider<WeakReference<Wheel?>?>?>?

    fun whellProvider(): Provider<Wheel?>?

    fun carLazy(): LazyProvide<Car?>?

    fun carProvider(): Provider<Car?>?

    fun carWeak(): WeakReference<Car?>?

    fun window(): Window?

    fun car(): Car?

    fun carAsync(): AsyncLazy<Car?>?

    fun bumperAsyncPhantom(): FantomAsyncProvide<Bumper>


}
