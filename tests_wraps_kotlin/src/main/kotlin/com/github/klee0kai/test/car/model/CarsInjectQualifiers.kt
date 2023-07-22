package com.github.klee0kai.test.car.model

import com.github.klee0kai.test.car.di.qualifiers.qualifiers.MyQualifier
import com.github.klee0kai.test.car.di.qualifiers.qualifiers.MyQualifierMulti
import com.github.klee0kai.test.car.di.qualifiers.qualifiers.MyQualifierWithString
import javax.inject.Inject
import javax.inject.Named

class CarsInjectQualifiers {
    @Inject
    @Named
    var carNamedEmpty: Car? = null

    @Inject
    @Named("a")
    var carNamedA: Car? = null

    @Inject
    @MyQualifier
    var carMyQualifier: Car? = null

    @Inject
    @MyQualifierWithString
    var carMyQualifierString: Car? = null

    @Inject
    @MyQualifierWithString(id = "a")
    var carMyQualifierA: Car? = null

    @Inject
    @MyQualifierWithString(id = "b")
    var carMyQualifierB: Car? = null

    @Inject
    @MyQualifierMulti
    var carMyQualifierMulti: Car? = null

    @Inject
    @MyQualifierMulti(indx = 1, id = "a")
    var carMyQualifierMultiA1: Car? = null

    @Inject
    @MyQualifierMulti(indx = 2, id = "a")
    var carMyQualifierMultiA2: Car? = null

    @Inject
    @MyQualifierMulti(type = MyQualifierMulti.Type.HARD, id = "a", indx = 2)
    var carMyQualifierMultiA2Hard: Car? = null

    @Inject
    var allCars: List<Car>? = null

    @Inject
    @Named
    var carsNamedEmpty: List<Car>? = null

    @Inject
    @MyQualifierMulti(type = MyQualifierMulti.Type.HARD, id = "a", indx = 2)
    var carsMyQualifierMultiA2Hard: List<Car>? = null


    var methodCarNamedEmpty: Car? = null
    var methodCarNamedA: Car? = null
    var methodCarMyQualifier: Car? = null
    var methodCarMyQualifierString: Car? = null
    var methodCarMyQualifierA: Car? = null
    var methodCarMyQualifierB: Car? = null
    var methodCarMyQualifierMulti: Car? = null
    var methodCarMyQualifierMultiA1: Car? = null
    var methodCarMyQualifierMultiA2: Car? = null
    var methodCarMyQualifierMultiA2Hard: Car? = null
    var methodAllCars: List<Car>? = null
    var methodCarsNamedEmpty: List<Car>? = null
    var methodCarsMyQualifierMultiA2Hard: List<Car>? = null

    @Inject
    fun init(
        @Named carNamedEmpty: Car?,
        @Named("a") carNamedA: Car?,
        @MyQualifier carMyQualifier: Car?,
        @MyQualifierWithString carMyQualifierString: Car?,
        @MyQualifierWithString(id = "a") carMyQualifierA: Car?,
        @MyQualifierWithString(id = "b") carMyQualifierB: Car?,
        @MyQualifierMulti carMyQualifierMulti: Car?,
        @MyQualifierMulti(indx = 1, id = "a") carMyQualifierMultiA1: Car?,
        @MyQualifierMulti(indx = 2, id = "a") carMyQualifierMultiA2: Car?,
        @MyQualifierMulti(type = MyQualifierMulti.Type.HARD, id = "a", indx = 2) carMyQualifierMultiA2Hard: Car?,
        allCars: List<Car>?,
        @Named carsNamedEmpty: List<Car>?,
        @MyQualifierMulti(type = MyQualifierMulti.Type.HARD, id = "a", indx = 2) carsMyQualifierMultiA2Hard: List<Car>?
    ) {
        methodCarNamedEmpty = carNamedEmpty
        methodCarNamedA = carNamedA
        methodCarMyQualifier = carMyQualifier
        methodCarMyQualifierString = carMyQualifierString
        methodCarMyQualifierA = carMyQualifierA
        methodCarMyQualifierB = carMyQualifierB
        methodCarMyQualifierMulti = carMyQualifierMulti
        methodCarMyQualifierMultiA1 = carMyQualifierMultiA1
        methodCarMyQualifierMultiA2 = carMyQualifierMultiA2
        methodCarMyQualifierMultiA2Hard = carMyQualifierMultiA2Hard
        methodAllCars = allCars
        methodCarsNamedEmpty = carsNamedEmpty
        methodCarsMyQualifierMultiA2Hard = carsMyQualifierMultiA2Hard
    }
}
