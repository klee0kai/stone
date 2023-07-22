package com.github.klee0kai.stone.test.car.inject

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.car.di.inject.CarInjectComponent
import com.github.klee0kai.test.car.model.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CarInjectMixedTests {
    @BeforeEach
    fun init() {
        Bumper.createCount = 0
        Window.createCount = 0
        Wheel.createCount = 0
        Car.createCount = 0
    }

    @Test
    fun carInjectReusableTest() {
        //Given
        val DI = Stone.createComponent(CarInjectComponent::class.java)

        //When
        val carInject1 = CarInject()
        val carInject2 = CarInject()
        DI.inject(carInject1)
        DI.inject(carInject2)


        // Then
        assertEquals(2, Bumper.createCount, "Bumpers created from list. And reused")
        assertEquals(carInject1.bumperFromMethod!!.uuid, carInject2.bumper!!.uuid, "should cache")
        assertEquals(carInject1.wheelFromMethod!!.uuid, carInject2.wheel!!.uuid, "should cache ")
        assertNotEquals(
            /* unexpected = */ carInject1.windowFromMethod!!.uuid,
            /* actual = */ carInject2.window!!.uuid,
            /* message = */ "provide via Provider. No caching"
        )
    }

    @Test
    fun carInjectListReusableTest() {
        //Given
        val DI = Stone.createComponent(CarInjectComponent::class.java)

        //When
        val carInject1 = CarInjectLists()
        val carInject2 = CarInjectLists()
        DI.inject(carInject1)
        DI.inject(carInject2)

        // Then
        assertEquals(2, carInject1.bumpers!!.size)
        assertEquals(5, carInject1.wheels!!.size)
        assertEquals(4, carInject1.windows!!.size)
        for (i in 0..1) assertEquals(carInject1.bumpersMethodFrom!![i].uuid, carInject2.bumpers!![i].uuid)
        for (i in 0..1) assertEquals(carInject1.wheelsMethodFrom!![i].uuid, carInject2.wheels!![i].uuid)
        val windowUids: MutableSet<String> = HashSet()
        for (i in 0..3) windowUids.add(carInject1.windowsMethodFrom!![i].uuid)
        for (i in 0..3) windowUids.add(carInject2.windows!![i].uuid)
        assertEquals(8, windowUids.size)
    }

    @Test
    fun carInjectProvideReusableTest() {
        //Given
        val DI = Stone.createComponent(CarInjectComponent::class.java)

        //When
        val carInject1 = CarInjectProvider()
        val carInject2 = CarInjectProvider()
        DI.inject(carInject1)
        DI.inject(carInject2)


        // Then
        assertEquals(0, Bumper.createCount, "Provider non used")
        assertEquals(
            carInject1.bumperFromMethod!!.get().uuid,
            carInject2.bumper!!.get().uuid,
            "should cache"
        )
        assertEquals(carInject1.wheelFromMethod!!.get().uuid, carInject2.wheel!!.get().uuid, "should cache ")
        assertNotEquals(
            carInject1.windowFromMethod!!.get().value!!.uuid,
            carInject2.window!!.get().value!!.uuid,
            "provide via Provider. No caching"
        )
        assertEquals(2, Bumper.createCount, "Bumpers created from list. And reused")
    }
}
