package com.github.klee0kai.stone.kotlin.test.car.qualifiers

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.car.di.qualifiers.CarQComponent
import com.github.klee0kai.test.car.model.Car
import com.github.klee0kai.test.car.model.CarsInjectQualifiers
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CarInjectAllWithQualifiersTest {
    @Test
    fun namedEmptyProvideTest() {
        //Given
        val DI = Stone.createComponent(CarQComponent::class.java)
        val carInject = CarsInjectQualifiers()

        //When
        DI.inject(carInject)
        val filtered: List<Car> = carInject.allCars!!.filter { it.qualifier == "named_empty" }
        val car = filtered[0]

        //Then
        assertEquals(1, filtered.size)
        assertEquals("simple", car.bumpers!![0]!!.qualifier)
        assertEquals(5, car.wheels!!.size, "four wheel +1")
    }

    @Test
    fun namedAProvideTest() {
        //Given
        val DI = Stone.createComponent(CarQComponent::class.java)
        val carInject = CarsInjectQualifiers()

        //When
        DI.inject(carInject)
        val filtered: List<Car> = carInject.allCars!!.filter { it.qualifier == "named_a" }
        val car = filtered[0]

        //Then
        assertEquals(1, filtered.size)
        assertEquals("reinforced", car.bumpers!![0]!!.qualifier)
        assertEquals(5, car.wheels!!.size, "four wheel +1")
    }

    @Test
    fun carMyQualifierProvideTest() {
        //Given
        val DI = Stone.createComponent(CarQComponent::class.java)
        val carInject = CarsInjectQualifiers()

        //When
        DI.inject(carInject)
        val filtered: List<Car> = carInject.allCars!!.filter { it.qualifier == "my_qualifier" }
        val car = filtered[0]

        //Then
        assertEquals(1, filtered.size)
        assertEquals("simple", car.bumpers!![0]!!.qualifier)
        assertEquals(5, car.wheels!!.size, "four wheel +1")
    }

    @Test
    fun carMyQualifierStringProvideTest() {
        //Given
        val DI = Stone.createComponent(CarQComponent::class.java)
        val carInject = CarsInjectQualifiers()

        //When
        DI.inject(carInject)
        val filtered: List<Car> = carInject.allCars!!.filter { it.qualifier == "my_qualifier_with_string" }
        val car = filtered[0]

        //Then
        assertEquals(1, filtered.size)
        assertEquals("simple", car.bumpers!![0]!!.qualifier)
        assertEquals(5, car.wheels!!.size, "four wheel +1")
    }

    @Test
    fun carMyQualifierStringAProvideTest() {
        //Given
        val DI = Stone.createComponent(CarQComponent::class.java)
        val carInject = CarsInjectQualifiers()

        //When
        DI.inject(carInject)
        val filtered: List<Car> = carInject.allCars!!.filter { it.qualifier == "my_qualifier_a" }
        val car = filtered[0]

        //Then
        assertEquals(1, filtered.size)
        assertEquals("reinforced", car.bumpers!![0]!!.qualifier)
        assertEquals(4, car.wheels!!.size, "four wheel +1")
    }

    @Test
    fun carMyQualifierStringBProvideTest() {
        //Given
        val DI = Stone.createComponent(CarQComponent::class.java)
        val carInject = CarsInjectQualifiers()

        //When
        DI.inject(carInject)
        val filtered: List<Car> = carInject.allCars!!.filter { it.qualifier == "my_qualifier_b" }
        val car = filtered[0]

        //Then
        assertEquals(1, filtered.size)
        assertEquals("simple", car.bumpers!![0]!!.qualifier)
        assertEquals(5, car.wheels!!.size, "four wheel +1")
    }

    @Test
    fun carMyQualifierMultiProvideTest() {
        //Given
        val DI = Stone.createComponent(CarQComponent::class.java)
        val carInject = CarsInjectQualifiers()

        //When
        DI.inject(carInject)
        val filtered: List<Car> = carInject.allCars!!.filter { it.qualifier == "qualifier_multi" }
        val car = filtered[0]

        //Then
        assertEquals(1, filtered.size)
        assertEquals("simple", car.bumpers!![0]!!.qualifier)
        assertEquals(4, car.wheels!!.size)
    }

    @Test
    fun carMyQualifierMultiA1ProvideTest() {
        //Given
        val DI = Stone.createComponent(CarQComponent::class.java)
        val carInject = CarsInjectQualifiers()

        //When
        DI.inject(carInject)
        val filtered: List<Car> = carInject.allCars!!.filter { it.qualifier == "qualifier_multi_a1" }
        val car = filtered[0]

        //Then
        assertEquals(1, filtered.size)
        assertEquals("simple", car.bumpers!![0]!!.qualifier)
        assertEquals(5, car.wheels!!.size, "four wheel +1")
    }

    @Test
    fun carMyQualifierMultiA2ProvideTest() {
        //Given
        val DI = Stone.createComponent(CarQComponent::class.java)
        val carInject = CarsInjectQualifiers()

        //When
        DI.inject(carInject)
        val filtered: List<Car> = carInject.allCars!!.filter { it.qualifier == "qualifier_multi_a2" }
        val car = filtered[0]

        //Then
        assertEquals(1, filtered.size)
        assertEquals("reinforced", car.bumpers!![0]!!.qualifier)
        assertEquals(4, car.wheels!!.size)
    }

    @Test
    fun carMyQualifierMultiA2HardProvideTest() {
        //Given
        val DI = Stone.createComponent(CarQComponent::class.java)
        val carInject = CarsInjectQualifiers()

        //When
        DI.inject(carInject)
        val filtered: List<Car> = carInject.allCars!!.filter { it.qualifier == "qualifier_multi_a2_hard" }
        val car = filtered[0]

        //Then
        assertEquals(1, filtered.size)
        assertEquals("simple", car.bumpers!![0]!!.qualifier)
        assertEquals(4, car.wheels!!.size)
    }

    @Test
    fun allCarsProvideTest() {
        //Given
        val DI = Stone.createComponent(CarQComponent::class.java)
        val carInject = CarsInjectQualifiers()

        //When
        DI.inject(carInject)

        //Then
        assertEquals(14, carInject.allCars!!.size)
        assertEquals(
            1,
            carInject.allCars!!.count { it.qualifier == "my_qualifier_a" }
        )
        assertEquals(
            1,
            carInject.allCars!!.count { it.qualifier == "qualifier_multi_a2" }
        )
        assertEquals(
            1,
            carInject.allCars!!.count { it.qualifier == "qualifier_multi_a2_hard" }
        )
    }
}
