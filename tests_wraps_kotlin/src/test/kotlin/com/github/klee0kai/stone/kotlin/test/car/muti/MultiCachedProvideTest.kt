package com.github.klee0kai.stone.kotlin.test.car.muti

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.car.di.lists.cached.CarMultiCachedComponent
import com.github.klee0kai.test.car.model.Bumper
import com.github.klee0kai.test.car.model.Car
import com.github.klee0kai.test.car.model.Wheel
import com.github.klee0kai.test.car.model.Window
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MultiCachedProvideTest {
    @BeforeEach
    fun init() {
        Bumper.createCount = 0
        Window.createCount = 0
        Wheel.createCount = 0
        Car.createCount = 0
    }

    @Test
    fun firstBumperFromCollection() {
        //Given
        val DI = Stone.createComponent(
            CarMultiCachedComponent::class.java
        )

        //When
        assertEquals(0, Bumper.createCount)
        val bumperProvide = DI.singleBumper()

        //Then
        assertEquals(0, Bumper.createCount)
        val bumper = bumperProvide!!.get()
        assertEquals(2, Bumper.createCount, "Bumper created via list")
        assertNotNull(bumper!!.uuid)
    }

    @Test
    fun cachedBumperFromCollection() {
        //Given
        val DI = Stone.createComponent(
            CarMultiCachedComponent::class.java
        )

        //When
        val bumperProvide = DI.singleBumper()
        val bumper1 = bumperProvide!!.get()
        val bumper2 = bumperProvide.get()
        val bumper3 = DI.singleBumper()!!.get()!!

        //Then
        assertEquals(2, Bumper.createCount, "Bumper created via list. Cached")
        assertNotNull(bumper1!!.uuid)
        assertEquals(bumper1.uuid, bumper2!!.uuid)
        assertEquals(bumper1.uuid, bumper3.uuid)
    }

    @Test
    fun fourWheelsAndSpare() {
        //Given
        val DI = Stone.createComponent(
            CarMultiCachedComponent::class.java
        )

        //When
        assertEquals(0, Wheel.createCount)
        val wheelsProviderList = DI.wheels()

        //Then
        // assertEquals(0, Wheel.createCount); // each item wrapping in list not delayed
        val wheels = wheelsProviderList!!.map { it!!.get()!!.get() }
        assertEquals(5, Wheel.createCount)
        assertEquals(5, wheels.size)
        for (w in wheels) assertNotNull(w!!.uuid)
        val wheelsUid: Set<String> = wheels.map { it!!.uuid }.toSet()
        assertEquals(5, wheelsUid.size)
    }

    @Test
    fun fourWheelsAndSpareCached() {
        //Given
        val DI = Stone.createComponent(CarMultiCachedComponent::class.java)

        //When
        val wheelsProviderList = DI.wheels()
        val wheels1 = wheelsProviderList!!.map { it!!.get()!!.get() }
        val wheels2 = wheelsProviderList.map { it!!.get()!!.get() }
        val wheels3 = wheelsProviderList.map { it!!.get()!!.get() }

        // Then
        assertEquals(5, Wheel.createCount, "wheel lists should cached")
        val wheelsUuid1 = wheels1.map { it!!.uuid }
        val wheelsUuid2 = wheels2.map { it!!.uuid }
        val wheelsUuid3 = wheels3.map { it!!.uuid }
        assertEquals(wheelsUuid1, wheelsUuid2)
        assertEquals(wheelsUuid1, wheelsUuid3)
    }

    @Test
    fun oneWheelFromList() {
        //Given
        val DI = Stone.createComponent(CarMultiCachedComponent::class.java)

        //When
        val wheel1 = DI.wheel()
        val wheel2 = DI.wheel()

        //Then
        assertNotNull(wheel1!!.uuid)
        assertEquals(wheel1.uuid, wheel2!!.uuid)
    }

    @Test
    fun allWindowsInCar() {
        //Given
        val DI = Stone.createComponent(CarMultiCachedComponent::class.java)

        //When
        assertEquals(0, Window.createCount)
        val windows = DI.windows()

        //Then
        assertEquals(4, Window.createCount)
        assertEquals(1, windows!!.size)
        assertEquals(4, windows[0]!!.size)
        for (w in windows[0]!!) assertNotNull(w!!.uuid)
        val windowUuid = windows[0]!!.map { it!!.uuid }.toSet()
        assertEquals(4, windowUuid.size)
    }

    @Test
    fun allWindowsInCarFactory() {
        //Given
        val DI = Stone.createComponent(CarMultiCachedComponent::class.java)

        //When
        assertEquals(0, Window.createCount)
        val windows1 = DI.windows()
        val windows2 = DI.windows()

        //Then
        assertEquals(
            8,
            Window.createCount,
            "Windows creating over provide wrappers. Should recreate each time"
        )
    }

    @Test
    fun allWindowsInCarProvideWrapper() {
        //Given
        val DI = Stone.createComponent(CarMultiCachedComponent::class.java)

        //When
        assertEquals(0, Window.createCount)
        val windows = DI.windowsProviding()

        //Then
        assertEquals(0, Window.createCount, "Provider non used.")
        assertEquals(1, windows!!.size)
        assertEquals(4, windows[0]!!.get()!!.size)
        assertEquals(4, Window.createCount, "Provider used. Windows created")
        for (w in windows[0]!!.get()!!) assertNotNull(w!!.uuid)
        val windowUuid: Set<String> = windows[0]!!.get()!!.map { it!!.uuid }.toSet()
        assertEquals(4, windowUuid.size)
    }

    @Test
    fun createCarsWithDeps() {
        //Given
        val DI = Stone.createComponent(CarMultiCachedComponent::class.java)

        //When
        assertEquals(0, Car.createCount)
        val car = DI.cars()

        //Then
        assertEquals(2, Car.createCount)
        Assertions.assertNotEquals(car!![0]!!.uuid, car[1]!!.uuid)
        assertNotNull(car[0]!!.bumpers)
        assertNotNull(car[0]!!.wheels)
        assertNotNull(car[0]!!.windows)
        assertNotNull(car[1]!!.bumpers)
        assertNotNull(car[1]!!.wheels)
        assertNotNull(car[1]!!.windows)
        Assertions.assertNotEquals(
            car[0]!!.windows!!.size,
            car[1]!!.windows!!.size,
            "Red use single deps and Blue car use listed deps"
        )
    }

    @Test
    fun cacheCreatedCar() {
        //Given
        val DI = Stone.createComponent(CarMultiCachedComponent::class.java)

        //When
        assertEquals(0, Car.createCount)
        val car1 = DI.cars()
        val car2 = DI.cars()

        //Then
        assertEquals(2, Car.createCount)
        assertEquals(car1!!.size, car2!!.size)
        assertEquals(car1[0]!!.uuid, car2[0]!!.uuid)
        assertEquals(car1[1]!!.uuid, car2[1]!!.uuid)
    }
}
