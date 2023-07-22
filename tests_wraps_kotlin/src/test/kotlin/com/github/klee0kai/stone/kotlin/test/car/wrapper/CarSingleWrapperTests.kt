package com.github.klee0kai.stone.kotlin.test.car.wrapper

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.car.di.wrapped.create.CarWrappedCreateComponent
import com.github.klee0kai.test.car.model.Bumper
import com.github.klee0kai.test.car.model.Car
import com.github.klee0kai.test.car.model.Wheel
import com.github.klee0kai.test.car.model.Window
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CarSingleWrapperTests {
    @BeforeEach
    fun init() {
        Bumper.createCount = 0
        Window.createCount = 0
        Wheel.createCount = 0
        Car.createCount = 0
    }

    @Test
    fun wheelSimpleTest() {
        //Given
        val DI = Stone.createComponent(CarWrappedCreateComponent::class.java)

        //When
        val wheel1 = DI.wheel()
        val wheel2 = DI.wheel()

        // Then
        assertEquals(1, Wheel.createCount)
        assertNotNull(wheel1!!.uuid)
        assertEquals(wheel1.uuid, wheel2!!.uuid)
    }

    @Test
    fun wheelProvideTest() {
        //Given
        val DI = Stone.createComponent(CarWrappedCreateComponent::class.java)

        //When
        val wheel1 = DI.wheelProvide()
        val wheel2 = DI.wheelProvide()

        // Then
        assertNotNull(wheel1!!.get()!!.uuid)
        assertEquals(wheel1.get()!!.uuid, wheel2!!.get()!!.uuid)
        assertEquals(1, Wheel.createCount)
    }

    @Test
    fun wheelLazyTest() {
        //Given
        val DI = Stone.createComponent(CarWrappedCreateComponent::class.java)

        //When
        val wheel1 = DI.wheelLazy()
        val wheel2 = DI.wheelLazy()

        // Then
        assertNotNull(wheel1!!.get()!!.uuid)
        assertEquals(wheel1.get()!!.uuid, wheel2!!.get()!!.uuid)
        assertEquals(1, Wheel.createCount)
    }

    @Test
    fun wheelWeakTest() {
        //Given
        val DI = Stone.createComponent(CarWrappedCreateComponent::class.java)

        //When
        val wheel1 = DI.wheelWeak()
        val wheel2 = DI.wheelWeak()

        // Then
        assertEquals(1, Wheel.createCount)
        assertNotNull(wheel1!!.get()!!.uuid)
        assertEquals(wheel1.get()!!.uuid, wheel2!!.get()!!.uuid)
    }

    @Test
    fun wheelProvideWeakTest() {
        //Given
        val DI = Stone.createComponent(CarWrappedCreateComponent::class.java)

        //When
        val wheel1 = DI.whellProviderWeak()
        val wheel2 = DI.whellProviderWeak()

        // Then
        assertNotNull(wheel1!!.get()!!.get()!!.uuid)
        assertEquals(wheel1.get()!!.get()!!.uuid, wheel2!!.get()!!.get()!!.uuid)
        assertEquals(1, Wheel.createCount)
    }

    @Test
    fun wheelLazyProvideWeakTest() {
        //Given
        val DI = Stone.createComponent(CarWrappedCreateComponent::class.java)

        //When
        val wheel1 = DI.whellLazyProviderWeak()
        val wheel2 = DI.whellLazyProviderWeak()

        // Then
        assertNotNull(wheel1!!.get()!!.get()!!.get()!!.uuid)
        assertEquals(
            wheel1.get()!!.get()!!.get()!!.uuid,
            wheel2!!.get()!!.get()!!.get()!!.uuid
        )
        assertEquals(1, Wheel.createCount)
    }

    @Test
    fun wheelProviderTest() {
        //Given
        val DI = Stone.createComponent(CarWrappedCreateComponent::class.java)

        //When
        val wheel1 = DI.whellProvider()
        val wheel2 = DI.whellProvider()

        // Then
        assertNotNull(wheel1!!.get()!!.uuid)
        assertEquals(wheel1.get()!!.uuid, wheel2!!.get()!!.uuid)
        assertEquals(1, Wheel.createCount)
    }

    @Test
    fun carLazyTest() {
        //Given
        val DI = Stone.createComponent(CarWrappedCreateComponent::class.java)

        //When
        val car1 = DI.carLazy()
        val car2 = DI.carLazy()

        // Then
        assertEquals(0, Car.createCount)
        assertNotNull(car1!!.get()!!.uuid)
        assertEquals(car1.get()!!.uuid, car2!!.get()!!.uuid)
        assertEquals(1, Car.createCount)
    }

    @Test
    fun carAsyncTest() {
        //Given
        val DI = Stone.createComponent(CarWrappedCreateComponent::class.java)

        //When
        val car1 = DI.carAsync()
        val car2 = DI.carAsync()

        // Then
        assertNotNull(car1!!.get()!!.uuid)
        assertEquals(car1.get()!!.uuid, car2!!.get()!!.uuid)
        assertEquals(1, Car.createCount)
    }

    @Test
    fun carProvideTest() {
        //Given
        val DI = Stone.createComponent(CarWrappedCreateComponent::class.java)

        //When
        val car1 = DI.carProvider()
        val car2 = DI.carProvider()

        // Then
        assertEquals(0, Car.createCount)
        assertNotNull(car1!!.get()!!.uuid)
        assertEquals(car1.get()!!.uuid, car2!!.get()!!.uuid)
        assertEquals(1, Car.createCount)
    }

    @Test
    fun carWeakTest() {
        //Given
        val DI = Stone.createComponent(CarWrappedCreateComponent::class.java)

        //When
        val car1 = DI.carWeak()
        val car2 = DI.carWeak()

        // Then
        assertEquals(1, Car.createCount)
        assertNotNull(car1!!.get()!!.uuid)
        assertEquals(car1.get()!!.uuid, car2!!.get()!!.uuid)
    }

    @Test
    fun windowTest() {
        //Given
        val DI = Stone.createComponent(CarWrappedCreateComponent::class.java)

        //When
        val window1 = DI.window()
        val window2 = DI.window()

        // Then
        assertEquals(1, Window.createCount)
        assertNotNull(window1!!.uuid)
        assertEquals(window1.uuid, window2!!.uuid)
    }

    @Test
    fun carTest() {
        //Given
        val DI = Stone.createComponent(CarWrappedCreateComponent::class.java)

        //When
        val car1 = DI.car()
        val car2 = DI.car()

        // Then
        assertEquals(1, Car.createCount)
        assertNotNull(car1!!.uuid)
        assertEquals(car1.uuid, car2!!.uuid)
    }
}
