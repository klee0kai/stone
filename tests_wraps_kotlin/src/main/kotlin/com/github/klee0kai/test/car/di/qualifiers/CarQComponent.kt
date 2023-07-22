package com.github.klee0kai.test.car.di.qualifiers

import com.github.klee0kai.stone.annotations.component.*
import com.github.klee0kai.test.car.di.qualifiers.qualifiers.MyQualifier
import com.github.klee0kai.test.car.di.qualifiers.qualifiers.MyQualifierMulti
import com.github.klee0kai.test.car.di.qualifiers.qualifiers.MyQualifierWithString
import com.github.klee0kai.test.car.model.Car
import com.github.klee0kai.test.car.model.CarsInjectQualifiers
import javax.inject.Named

@Component
interface CarQComponent {
    fun module1(): CarQPModule?
    fun module2(): CarQCModule?

    @Named
    fun carNamedEmpty(): Car?

    @Named("a")
    fun carNameA(): Car?

    @MyQualifier
    fun carMyQualifier(): Car?

    @MyQualifierWithString
    fun carMyQualifierString(): Car?

    @MyQualifierWithString(id = "a")
    fun carMyQualifierStringA(): Car?

    @MyQualifierWithString(id = "b")
    fun carMyQualifierStringB(): Car?

    @MyQualifierMulti
    fun carMyQualifierMulti(): Car?

    @MyQualifierMulti(indx = 1, id = "a")
    fun carMyQualifierMultiA1(): Car?

    @MyQualifierMulti(id = "a", indx = 2)
    fun carMyQualifierMultiA2(): Car?

    @MyQualifierMulti(type = MyQualifierMulti.Type.HARD, indx = 2, id = "a")
    fun carMyQualifierMultiA2Hard(): Car?
    fun allCars(): List<Car?>?

    @Named
    fun provideCarsNamedEmpty(): List<Car?>?

    @MyQualifierMulti(type = MyQualifierMulti.Type.HARD, indx = 2, id = "a")
    fun carsMyQualifierMultiA2Hard(): List<Car?>?
    fun inject(carInject: CarsInjectQualifiers?)
}
