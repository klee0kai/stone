package com.github.klee0kai.stone.test.car.inject

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.stone._hidden_.types.ListUtils
import com.github.klee0kai.test.car.di.inject.CarInjectComponent
import com.github.klee0kai.test.car.model.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CarInjectMethodTests {
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
        assertNotNull(carInject.bumperFromMethod!!.uuid)
        assertNotNull(carInject.wheelFromMethod!!.uuid)
        assertNotNull(carInject.windowFromMethod!!.uuid)
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
        assertEquals(carInject1.bumperFromMethod!!.uuid, carInject2.bumperFromMethod!!.uuid, "should cache")
        assertEquals(carInject1.wheelFromMethod!!.uuid, carInject2.wheelFromMethod!!.uuid, "should cache ")
        assertNotEquals(
            carInject1.windowFromMethod!!.uuid,
            carInject2.windowFromMethod!!.uuid,
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
        assertEquals(8, Window.createCount, "Windows created for fields and method")
        assertEquals(2, carInject.bumpersMethodFrom!!.size)
        assertEquals(5, carInject.wheelsMethodFrom!!.size)
        assertEquals(4, carInject.windowsMethodFrom!!.size)
        for (b in carInject.bumpersMethodFrom!!) assertNotNull(b.uuid)
        val bumperUids: Set<String> = HashSet(ListUtils.format(carInject.bumpersMethodFrom) { it: Bumper -> it.uuid })
        assertEquals(2, bumperUids.size)
        for (w in carInject.wheelsMethodFrom!!) assertNotNull(w.uuid)
        val wheelsUid: Set<String> = HashSet(ListUtils.format(carInject.wheels) { it: Wheel -> it.uuid })
        assertEquals(5, wheelsUid.size)
        for (w in carInject.windowsMethodFrom!!) assertNotNull(w.uuid)
        val windowUids: Set<String> = HashSet(ListUtils.format(carInject.windows) { it: Window -> it.uuid })
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
        assertEquals(2, carInject1.bumpersMethodFrom!!.size)
        assertEquals(5, carInject1.wheelsMethodFrom!!.size)
        assertEquals(4, carInject1.windowsMethodFrom!!.size)
        for (i in 0..1) assertEquals(
            carInject1.bumpersMethodFrom!![i].uuid,
            carInject2.bumpersMethodFrom!![i].uuid
        )
        for (i in 0..1) assertEquals(
            carInject1.wheelsMethodFrom!![i].uuid,
            carInject2.wheelsMethodFrom!![i].uuid
        )
        val windowUids: MutableSet<String> = HashSet()
        for (i in 0..3) windowUids.add(carInject1.windowsMethodFrom!![i].uuid)
        for (i in 0..3) windowUids.add(carInject2.windowsMethodFrom!![i].uuid)
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
        assertNotNull(carInject.bumperFromMethod!!.get().uuid)
        assertNotNull(carInject.wheelFromMethod!!.get().uuid)
        assertNotNull(carInject.windowFromMethod!!.get().value!!.uuid)
        assertEquals(2, Bumper.createCount, "Bumpers created from list")
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
            carInject2.bumperFromMethod!!.get().uuid,
            "should cache"
        )
        assertEquals(
            carInject1.wheelFromMethod!!.get().uuid,
            carInject2.wheelFromMethod!!.get().uuid,
            "should cache "
        )
        Assertions.assertNotEquals(
            carInject1.windowFromMethod!!.get().value!!.uuid,
            carInject2.windowFromMethod!!.get().value!!.uuid,
            "provide via Provider. No caching"
        )
        assertEquals(2, Bumper.createCount, "Bumpers created from list. And reused")
    }
}
