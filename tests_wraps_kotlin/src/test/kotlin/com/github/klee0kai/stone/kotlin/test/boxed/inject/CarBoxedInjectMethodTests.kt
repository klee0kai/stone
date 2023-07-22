package com.github.klee0kai.stone.kotlin.test.boxed.inject

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.boxed.di.inject.CarBoxedInjectComponent
import com.github.klee0kai.test.boxed.model.CarBoxedInject
import com.github.klee0kai.test.boxed.model.CarBoxedInjectLists
import com.github.klee0kai.test.boxed.model.CarBoxedInjectProvider
import com.github.klee0kai.test.car.model.Bumper
import com.github.klee0kai.test.car.model.Car
import com.github.klee0kai.test.car.model.Wheel
import com.github.klee0kai.test.car.model.Window
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CarBoxedInjectMethodTests {
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
        assertNotNull(carInject.bumperFromMethod!!.value.uuid)
        assertNotNull(carInject.wheelFromMethod!!.value.uuid)
        assertNotNull(carInject.windowFromMethod!!.value.uuid)
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
        assertEquals(
            carInject1.bumperFromMethod!!.value.uuid,
            carInject2.bumperFromMethod!!.value.uuid,
            "should cache"
        )
        assertEquals(
            carInject1.wheelFromMethod!!.value.uuid,
            carInject2.wheelFromMethod!!.value.uuid,
            "should cache "
        )
        Assertions.assertNotEquals(
            carInject1.windowFromMethod!!.value.uuid,
            carInject2.windowFromMethod!!.value.uuid,
            "provide via Provider. No caching"
        )
    }

    @Test
    fun carInjectListTest() {
        //Given
        val DI = Stone.createComponent(CarBoxedInjectComponent::class.java)

        //When
        val carInject = CarBoxedInjectLists()
        DI.inject(carInject)

        // Then
        assertEquals(2, Bumper.createCount)
        assertEquals(5, Wheel.createCount)
        assertEquals(8, Window.createCount, "Windows created for fields and method")
        assertEquals(2, carInject.bumpersMethodFrom!!.size)
        assertEquals(5, carInject.wheelsMethodFrom!!.size)
        assertEquals(4, carInject.windowsMethodFrom!!.size)
        for (b in carInject.bumpersMethodFrom!!) assertNotNull(b.value.uuid)
        val bumperUids: Set<String> = carInject.bumpersMethodFrom!!.map { it.value.uuid }.toSet()
        assertEquals(2, bumperUids.size)
        for (w in carInject.wheelsMethodFrom!!) assertNotNull(w.value.uuid)
        val wheelsUid: Set<String> = carInject.wheels!!.map { it.value.uuid }.toSet()
        assertEquals(5, wheelsUid.size)
        for (w in carInject.windowsMethodFrom!!) assertNotNull(w.value.uuid)
        val windowUids: Set<String> = carInject.windows!!.map { it.value.uuid }.toSet()
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
        assertEquals(2, carInject1.bumpersMethodFrom!!.size)
        assertEquals(5, carInject1.wheelsMethodFrom!!.size)
        assertEquals(4, carInject1.windowsMethodFrom!!.size)
        for (i in 0..1) assertEquals(
            carInject1.bumpersMethodFrom!![i].value.uuid,
            carInject2.bumpersMethodFrom!![i].value.uuid
        )
        for (i in 0..1) assertEquals(
            carInject1.wheelsMethodFrom!![i].value.uuid,
            carInject2.wheelsMethodFrom!![i].value.uuid
        )
        val windowUids: MutableSet<String> = HashSet()
        for (i in 0..3) windowUids.add(carInject1.windowsMethodFrom!![i].value.uuid)
        for (i in 0..3) windowUids.add(carInject2.windowsMethodFrom!![i].value.uuid)
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
        assertNotNull(carInject.bumperFromMethod!!.get().value.uuid)
        assertNotNull(carInject.wheelFromMethod!!.get().value.uuid)
        assertNotNull(carInject.windowFromMethod!!.get().value!!.value.uuid)
        assertEquals(2, Bumper.createCount, "Bumpers created from list")
    }

    @Test
    fun carInjectProvideReusableTest() {
        //Given
        val DI = Stone.createComponent(
            CarBoxedInjectComponent::class.java
        )

        //When
        val carInject1 = CarBoxedInjectProvider()
        val carInject2 = CarBoxedInjectProvider()
        DI.inject(carInject1)
        DI.inject(carInject2)


        // Then
        assertEquals(0, Bumper.createCount, "Provider non used")
        assertEquals(
            carInject1.bumperFromMethod!!.get().value.uuid,
            carInject2.bumperFromMethod!!.get().value.uuid,
            "should cache"
        )
        assertEquals(
            carInject1.wheelFromMethod!!.get().value.uuid,
            carInject2.wheelFromMethod!!.get().value.uuid,
            "should cache "
        )
        assertNotEquals(
            carInject1.windowFromMethod!!.get().value!!.value.uuid,
            carInject2.windowFromMethod!!.get().value!!.value.uuid,
            "provide via Provider. No caching"
        )
        assertEquals(2, Bumper.createCount, "Bumpers created from list. And reused")
    }
}
