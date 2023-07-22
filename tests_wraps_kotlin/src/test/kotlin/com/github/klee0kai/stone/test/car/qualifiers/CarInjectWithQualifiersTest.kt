package com.github.klee0kai.stone.test.car.qualifiers

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.car.di.qualifiers.CarQComponent
import com.github.klee0kai.test.car.model.CarsInjectQualifiers
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CarInjectWithQualifiersTest {
    @Test
    fun namedEmptyProvideTest() {
        //Given
        val DI = Stone.createComponent(CarQComponent::class.java)
        val carInject = CarsInjectQualifiers()

        //When
        DI.inject(carInject)
        val car = carInject.carNamedEmpty

        //Then
        assertEquals("named_empty", car!!.qualifier)
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
        val car = carInject.carNamedA

        //Then
        assertEquals("named_a", car!!.qualifier)
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
        val car = carInject.carMyQualifier

        //Then
        assertEquals("my_qualifier", car!!.qualifier)
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
        val car = carInject.carMyQualifierString

        //Then
        assertEquals("my_qualifier_with_string", car!!.qualifier)
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
        val car = carInject.carMyQualifierA

        //Then
        assertEquals("my_qualifier_a", car!!.qualifier)
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
        val car = carInject.carMyQualifierB

        //Then
        assertEquals("my_qualifier_b", car!!.qualifier)
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
        val car = carInject.carMyQualifierMulti

        //Then
        assertEquals("qualifier_multi", car!!.qualifier)
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
        val car = carInject.carMyQualifierMultiA1

        //Then
        assertEquals("qualifier_multi_a1", car!!.qualifier)
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
        val car = carInject.carMyQualifierMultiA2

        //Then
        assertEquals("qualifier_multi_a2", car!!.qualifier)
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
        val car = carInject.carMyQualifierMultiA2Hard

        //Then
        assertEquals("qualifier_multi_a2_hard", car!!.qualifier)
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

    @Test
    fun provideCarsNamedEmptyTest() {
        //Given
        val DI = Stone.createComponent(CarQComponent::class.java)
        val carInject = CarsInjectQualifiers()

        //When
        DI.inject(carInject)

        //Then
        assertEquals(1, carInject.carsNamedEmpty!!.size)
        assertEquals("named_empty", carInject.carsNamedEmpty!![0].qualifier)
    }

    @Test
    fun carsMyQualifierMultiA2HardTest() {
        //Given
        val DI = Stone.createComponent(CarQComponent::class.java)
        val carInject = CarsInjectQualifiers()

        //When
        DI.inject(carInject)

        //Then
        assertEquals(1, carInject.carsMyQualifierMultiA2Hard!!.size)
        assertEquals("qualifier_multi_a2_hard", carInject.carsMyQualifierMultiA2Hard!![0].qualifier)
    }
}
