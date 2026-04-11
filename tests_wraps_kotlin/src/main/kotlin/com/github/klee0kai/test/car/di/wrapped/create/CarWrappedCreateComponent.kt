package com.github.klee0kai.test.car.di.wrapped.create

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.wrappers.AsyncLazy
import com.github.klee0kai.stone.wrappers.AsyncProvider
import com.github.klee0kai.stone.wrappers.LazyProvider
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

    fun wheelLazy(): LazyProvider<Wheel?>?

    fun wheelWeak(): WeakReference<Wheel?>?

    fun whellProviderWeak(): Provider<WeakReference<Wheel?>?>?

    fun whellLazyProviderWeak(): LazyProvider<Provider<WeakReference<Wheel?>?>?>?

    fun whellProvider(): Provider<Wheel?>?

    fun carLazy(): LazyProvider<Car?>?

    fun carProvider(): Provider<Car?>?

    fun carWeak(): WeakReference<Car?>?

    fun window(): Window?

    fun car(): Car?

    fun carAsync(): AsyncLazy<Car?>?

    fun bumperAsyncPhantom(): AsyncProvider<Bumper>


}
