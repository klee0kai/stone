package com.github.klee0kai.stone.test.car.inject

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.car.di.inject.CarInjectComponent
import com.github.klee0kai.test.car.model.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CarInjectTests {
    @BeforeEach
    fun init() {
        Bumper.createCount = 0
        Window.createCount = 0
        Wheel.createCount = 0
        Car.createCount = 0
    }

    @Test
    fun carInjectTest() {
        //Given
        val DI = Stone.createComponent(CarInjectComponent::class.java)

        //When
        val carInject = CarInject()
        DI.inject(carInject)

        // Then
        assertEquals(2, Bumper.createCount, "Bumpers created from list")
        assertNotNull(carInject.bumper!!.uuid)
        assertNotNull(carInject.wheel!!.uuid)
        assertNotNull(carInject.window!!.uuid)
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
        assertEquals(carInject1.bumper!!.uuid, carInject2.bumper!!.uuid, "should cache")
        assertEquals(carInject1.wheel!!.uuid, carInject2.wheel!!.uuid, "should cache ")
        assertNotEquals(
            carInject1.window!!.uuid,
            carInject2.window!!.uuid,
            "provide via Provider. No caching"
        )
    }

    @Test
    fun carInjectListTest() {
        //Given
        val DI = Stone.createComponent(CarInjectComponent::class.java)

        //When
        val carInject = CarInjectLists()
        DI.inject(carInject)

        // Then
        assertEquals(2, Bumper.createCount)
        assertEquals(5, Wheel.createCount)
        assertEquals(8, Window.createCount, "Window created for fields and method")
        assertEquals(2, carInject.bumpers!!.size)
        assertEquals(5, carInject.wheels!!.size)
        assertEquals(4, carInject.windows!!.size)
        for (b in carInject.bumpers!!) assertNotNull(b.uuid)
        val bumperUids = carInject.bumpers!!.map { it.uuid }.toSet()
        assertEquals(2, bumperUids.size)
        for (w in carInject.wheels!!) assertNotNull(w.uuid)
        val wheelsUid = carInject.wheels!!.map { it.uuid }.toSet()
        assertEquals(5, wheelsUid.size)
        for (w in carInject.windows!!) assertNotNull(w.uuid)
        val windowUids = carInject.windows!!.map { it: Window -> it.uuid }.toSet()
        assertEquals(4, windowUids.size)
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
        for (i in 0..1) assertEquals(carInject1.bumpers!![i].uuid, carInject2.bumpers!![i].uuid)
        for (i in 0..1) assertEquals(carInject1.wheels!![i].uuid, carInject2.wheels!![i].uuid)
        val windowUids: MutableSet<String> = HashSet()
        for (i in 0..3) windowUids.add(carInject1.windows!![i].uuid)
        for (i in 0..3) windowUids.add(carInject2.windows!![i].uuid)
        assertEquals(8, windowUids.size)
    }

    @Test
    fun carInjectProviderTest() {
        //Given
        val DI = Stone.createComponent(CarInjectComponent::class.java)

        //When
        val carInject = CarInjectProvider()
        DI.inject(carInject)

        // Then
        assertEquals(0, Bumper.createCount, "Provide non used")
        assertNotNull(carInject.bumper!!.get().uuid)
        assertNotNull(carInject.wheel!!.get().uuid)
        assertNotNull(carInject.window!!.get().value!!.uuid)
        assertEquals(2, Bumper.createCount, "Bumpers created from list")
    }

    @Test
    fun carInjectProvideReusableTest() {
        //Given
        val DI = Stone.createComponent(
            CarInjectComponent::class.java
        )

        //When
        val carInject1 = CarInjectProvider()
        val carInject2 = CarInjectProvider()
        DI.inject(carInject1)
        DI.inject(carInject2)


        // Then
        assertEquals(0, Bumper.createCount, "Provider non used")
        assertEquals(carInject1.bumper!!.get().uuid, carInject2.bumper!!.get().uuid, "should cache")
        assertEquals(carInject1.wheel!!.get().uuid, carInject2.wheel!!.get().uuid, "should cache ")
        assertNotEquals(
            carInject1.window!!.get().value!!.uuid,
            carInject2.window!!.get().value!!.uuid,
            "provide via Provider. No caching"
        )
        assertEquals(2, Bumper.createCount, "Bumpers created from list. And reused")
    }
}
