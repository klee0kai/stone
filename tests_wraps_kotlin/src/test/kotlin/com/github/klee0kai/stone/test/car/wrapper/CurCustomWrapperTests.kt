package com.github.klee0kai.stone.test.car.wrapper

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.car.di.wrapped.custom.CarCustomWrappersComponent
import com.github.klee0kai.test.car.model.Car
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CurCustomWrapperTests {
    @BeforeEach
    fun init() {
        Car.createCount = 0
    }

    @Test
    fun carTest() {
        //Given
        val DI = Stone.createComponent(CarCustomWrappersComponent::class.java)

        //When
        val car1 = DI.car()
        val car2 = DI.car()

        // Then
        assertEquals(2, Car.createCount)
        assertNotNull(car1!!.uuid)
        assertNotEquals(car1.uuid, car2!!.uuid, "factory create")
    }

    @Test
    fun carLayTest() {
        //Given
        val DI = Stone.createComponent(CarCustomWrappersComponent::class.java)

        //When
        val car1 = DI.carLazy()
        val car2 = DI.carLazy()

        // Then
        assertEquals(0, Car.createCount)
        assertNotNull(car1!!.value!!.uuid)
        assertEquals(car1.value!!.uuid, car1.value!!.uuid)
        assertNotEquals(car1.value!!.uuid, car2!!.value!!.uuid)
        assertEquals(2, Car.createCount)
    }

    @Test
    fun carProvideTest() {
        //Given
        val DI = Stone.createComponent(CarCustomWrappersComponent::class.java)

        //When
        val car1 = DI.carProvide()
        val car2 = DI.carProvide()

        // Then
        assertEquals(0, Car.createCount)
        assertNotNull(car1!!.value!!.uuid)
        assertNotEquals(car1.value!!.uuid, car1.value!!.uuid)
        assertNotEquals(car1.value!!.uuid, car2!!.value!!.uuid)
        assertEquals(5, Car.createCount, "getValue has been called 5 times")
    }

    @Test
    fun carRefTest() {
        //Given
        val DI = Stone.createComponent(CarCustomWrappersComponent::class.java)

        //When
        val car1 = DI.carRef()
        val car2 = DI.carRef()

        // Then
        assertEquals(2, Car.createCount)
        assertNotNull(car1!!.value!!.uuid)
        assertNotEquals(car1.value!!.uuid, car2!!.value!!.uuid)
        assertEquals(car1.value!!.uuid, car1.value!!.uuid)
    }
}
