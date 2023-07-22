package com.github.klee0kai.stone.test.boxed.inject

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.stone._hidden_.types.ListUtils
import com.github.klee0kai.test.boxed.di.inject.CarBoxedInjectComponent
import com.github.klee0kai.test.boxed.model.CarBox
import com.github.klee0kai.test.boxed.model.CarBoxedInject
import com.github.klee0kai.test.boxed.model.CarBoxedInjectLists
import com.github.klee0kai.test.boxed.model.CarBoxedInjectProvider
import com.github.klee0kai.test.car.model.Bumper
import com.github.klee0kai.test.car.model.Car
import com.github.klee0kai.test.car.model.Wheel
import com.github.klee0kai.test.car.model.Window
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CarBoxedInjectTests {
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
        val DI = Stone.createComponent(CarBoxedInjectComponent::class.java)

        //When
        val carInject = CarBoxedInject()
        DI.inject(carInject)

        // Then
        assertEquals(2, Bumper.createCount, "Bumpers created from list")
        assertNotNull(carInject.bumper.value.uuid)
        assertNotNull(carInject.wheel.value.uuid)
        assertNotNull(carInject.window.value.uuid)
    }

    @Test
    fun carInjectReusableTest() {
        //Given
        val DI = Stone.createComponent(
            CarBoxedInjectComponent::class.java
        )

        //When
        val carInject1 = CarBoxedInject()
        val carInject2 = CarBoxedInject()
        DI.inject(carInject1)
        DI.inject(carInject2)


        // Then
        assertEquals(2, Bumper.createCount, "Bumpers created from list. And reused")
        assertEquals(carInject1.bumper.value.uuid, carInject2.bumper.value.uuid, "should cache")
        assertEquals(carInject1.wheel.value.uuid, carInject2.wheel.value.uuid, "should cache ")
        assertNotEquals(
            carInject1.window.value.uuid,
            carInject2.window.value.uuid,
            "provide via Provider. No caching"
        )
    }

    @Test
    fun carInjectListTest() {
        //Given
        val DI = Stone.createComponent(
            CarBoxedInjectComponent::class.java
        )

        //When
        val carInject = CarBoxedInjectLists()
        DI.inject(carInject)

        // Then
        assertEquals(2, Bumper.createCount)
        assertEquals(5, Wheel.createCount)
        assertEquals(8, Window.createCount, "Window created for fields and method")
        assertEquals(2, carInject.bumpers!!.size)
        assertEquals(5, carInject.wheels!!.size)
        assertEquals(4, carInject.windows!!.size)
        for (b in carInject.bumpers!!) assertNotNull(b.value.uuid)
        val bumperUids: Set<String> =
            HashSet(ListUtils.format(carInject.bumpers) { it: CarBox<Bumper> -> it.value.uuid })
        assertEquals(2, bumperUids.size)
        for (w in carInject.wheels!!) assertNotNull(w.value.uuid)
        val wheelsUid: Set<String> = HashSet(ListUtils.format(carInject.wheels) { it: CarBox<Wheel> -> it.value.uuid })
        assertEquals(5, wheelsUid.size)
        for (w in carInject.windows!!) assertNotNull(w.value.uuid)
        val windowUids: Set<String> =
            HashSet(ListUtils.format(carInject.windows) { it: CarBox<Window> -> it.value.uuid })
        assertEquals(4, windowUids.size)
    }

    @Test
    fun carInjectListReusableTest() {
        //Given
        val DI = Stone.createComponent(CarBoxedInjectComponent::class.java)

        //When
        val carInject1 = CarBoxedInjectLists()
        val carInject2 = CarBoxedInjectLists()
        DI.inject(carInject1)
        DI.inject(carInject2)

        // Then
        assertEquals(2, carInject1.bumpers!!.size)
        assertEquals(5, carInject1.wheels!!.size)
        assertEquals(4, carInject1.windows!!.size)
        for (i in 0..1) assertEquals(carInject1.bumpers!![i].value.uuid, carInject2.bumpers!![i].value.uuid)
        for (i in 0..1) assertEquals(carInject1.wheels!![i].value.uuid, carInject2.wheels!![i].value.uuid)
        val windowUids: MutableSet<String> = HashSet()
        for (i in 0..3) windowUids.add(carInject1.windows!![i].value.uuid)
        for (i in 0..3) windowUids.add(carInject2.windows!![i].value.uuid)
        assertEquals(8, windowUids.size)
    }

    @Test
    fun carInjectProviderTest() {
        //Given
        val DI = Stone.createComponent(CarBoxedInjectComponent::class.java)

        //When
        val carInject = CarBoxedInjectProvider()
        DI.inject(carInject)

        // Then
        assertEquals(0, Bumper.createCount, "Provide non used")
        assertNotNull(carInject.bumper!!.get().value.uuid)
        assertNotNull(carInject.wheel!!.get().value.uuid)
        assertNotNull(carInject.window!!.get().value!!.value.uuid)
        assertEquals(2, Bumper.createCount, "Bumpers created from list")
    }

    @Test
    fun carInjectProvideReusableTest() {
        //Given
        val DI = Stone.createComponent(CarBoxedInjectComponent::class.java)

        //When
        val carInject1 = CarBoxedInjectProvider()
        val carInject2 = CarBoxedInjectProvider()
        DI.inject(carInject1)
        DI.inject(carInject2)


        // Then
        assertEquals(0, Bumper.createCount, "Provider non used")
        assertEquals(
            carInject1.bumper!!.get().value.uuid,
            carInject2.bumper!!.get().value.uuid,
            "should cache"
        )
        assertEquals(
            carInject1.wheel!!.get().value.uuid,
            carInject2.wheel!!.get().value.uuid,
            "should cache "
        )
        assertNotEquals(
            carInject1.window!!.get().value!!.value.uuid,
            carInject2.window!!.get().value!!.value.uuid,
            "provide via Provider. No caching"
        )
        assertEquals(2, Bumper.createCount, "Bumpers created from list. And reused")
    }
}
