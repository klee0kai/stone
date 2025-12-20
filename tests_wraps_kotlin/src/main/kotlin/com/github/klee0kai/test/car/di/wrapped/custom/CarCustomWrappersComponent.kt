package com.github.klee0kai.test.car.di.wrapped.custom

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.test.boxed.di.inject.CarBoxedWrapper
import com.github.klee0kai.test.car.di.wrapped.custom.wrappers.*
import com.github.klee0kai.test.car.model.Car

@Component(
    wrapperHelpers = [CarBoxedWrapper::class, CarRefWrapper::class],
)
interface CarCustomWrappersComponent {
    fun module(): CarCustomWrapperModule?
    fun car(): Car?
    fun carRef(): CarRef<Car?>?
    fun carLazy(): CarLazy<Car?>?
    fun carProvide(): CarProvide<Car?>?
}
