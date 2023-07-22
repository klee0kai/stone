package com.github.klee0kai.stone.test.car.inject

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.car.di.inject.CarInjectComponent
import com.github.klee0kai.test.car.model.CarInject
import com.github.klee0kai.test.car.model.CarInjectLists
import com.github.klee0kai.test.car.model.CarInjectProvider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class CarProtectInjectedTests {
    @Test
    fun carProtectInjectedTest() {
        //Given
        val DI = Stone.createComponent(CarInjectComponent::class.java)

        //When
        var carInject: CarInject? = CarInject()
        DI.inject(carInject)
        val bumperUid = carInject!!.bumper!!.uuid
        val wheelUid = carInject.wheel!!.uuid
        val windowUid = carInject.window!!.uuid
        DI.protect(carInject)
        carInject = null
        System.gc()
        carInject = CarInject()
        DI.inject(carInject)


        // Then
        assertEquals(bumperUid, carInject.bumper!!.uuid, "Providing with caching")
        assertEquals(wheelUid, carInject.wheel!!.uuid, "Providing with caching")
        assertNotEquals(windowUid, carInject.window!!.uuid, "Providing without caching")
    }

    @Test
    fun carProtectListInjectedTest() {
        val DI = Stone.createComponent(CarInjectComponent::class.java)

        //When
        var carInject: CarInjectLists? = CarInjectLists()
        DI.inject(carInject)
        val bumperUids = carInject!!.bumpers!!.map { it.uuid }
        val wheelUids = carInject.wheels!!.map { it.uuid }
        val windowUids = carInject.windows!!.map { it.uuid }
        DI.protect(carInject)
        carInject = null
        System.gc()
        carInject = CarInjectLists()
        DI.inject(carInject)


        // Then
        assertEquals(
            bumperUids,
            carInject.bumpers!!.map { it.uuid },
            "Providing with caching"
        )
        assertEquals(
            wheelUids,
            carInject.wheels!!.map { it.uuid },
            "Providing with caching"
        )
        assertNotEquals(
            windowUids,
            carInject.windows!!.map { it.uuid },
            "Providing without caching"
        )
    }

    @Test
    fun carProtectProviderTest() {
        //Given
        val DI = Stone.createComponent(CarInjectComponent::class.java)

        //When
        var carInject: CarInjectProvider? = CarInjectProvider()
        DI.inject(carInject)
        val bumperUid = carInject!!.bumper!!.get().uuid
        val wheelUid = carInject.wheel!!.get().uuid
        val windowUid = carInject.window!!.get().value!!.uuid
        DI.protect(carInject)
        carInject = null
        System.gc()
        carInject = CarInjectProvider()
        DI.inject(carInject)


        // Then
        assertEquals(bumperUid, carInject.bumper!!.get().uuid, "Lazy Provider caching and can be protected")
        assertNotEquals(wheelUid, carInject.wheel!!.get().uuid, "Ref is non caching and non should protect")
        assertNotEquals(
            windowUid,
            carInject.window!!.get().value!!.uuid,
            "LazyProvide non support caching. Non should protect"
        )
    }
}
