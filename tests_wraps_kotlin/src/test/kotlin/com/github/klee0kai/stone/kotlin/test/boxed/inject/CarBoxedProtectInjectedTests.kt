package com.github.klee0kai.stone.kotlin.test.boxed.inject

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.stone._hidden_.types.ListUtils
import com.github.klee0kai.test.boxed.di.inject.CarBoxedInjectComponent
import com.github.klee0kai.test.boxed.model.CarBox
import com.github.klee0kai.test.boxed.model.CarBoxedInject
import com.github.klee0kai.test.boxed.model.CarBoxedInjectLists
import com.github.klee0kai.test.boxed.model.CarBoxedInjectProvider
import com.github.klee0kai.test.car.model.Bumper
import com.github.klee0kai.test.car.model.Wheel
import com.github.klee0kai.test.car.model.Window
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class CarBoxedProtectInjectedTests {
    @Test
    fun carProtectInjectedTest() {
        //Given
        val DI = Stone.createComponent(CarBoxedInjectComponent::class.java)

        //When
        var carInject: CarBoxedInject?
        carInject = CarBoxedInject()
        DI.inject(carInject)
        val bumperUid = carInject.bumper.value.uuid
        val wheelUid = carInject.wheel.value.uuid
        val windowUid = carInject.window.value.uuid
        DI.protect(carInject)
        carInject = null
        System.gc()
        carInject = CarBoxedInject()
        DI.inject(carInject)


        // Then
        assertEquals(bumperUid, carInject.bumper.value.uuid, "Providing with caching")
        assertEquals(wheelUid, carInject.wheel.value.uuid, "Providing with caching")
        assertNotEquals(windowUid, carInject.window.value.uuid, "Providing without caching")
    }

    @Test
    fun carProtectListInjectedTest() {
        val DI = Stone.createComponent(
            CarBoxedInjectComponent::class.java
        )

        //When
        var carInject: CarBoxedInjectLists? = CarBoxedInjectLists()
        DI.inject(carInject!!)
        val bumperUids = ListUtils.format(
            carInject.bumpers
        ) { it: CarBox<Bumper> -> it.value.uuid }
        val wheelUids = ListUtils.format(
            carInject.wheels
        ) { it: CarBox<Wheel> -> it.value.uuid }
        val windowUids = ListUtils.format(
            carInject.windows
        ) { it: CarBox<Window> -> it.value.uuid }
        DI.protect(carInject)
        carInject = null
        System.gc()
        carInject = CarBoxedInjectLists()
        DI.inject(carInject)


        // Then
        assertEquals(
            bumperUids,
            ListUtils.format(carInject.bumpers) { it: CarBox<Bumper> -> it.value.uuid },
            "Providing with caching"
        )
        assertEquals(
            wheelUids,
            ListUtils.format(carInject.wheels) { it: CarBox<Wheel> -> it.value.uuid },
            "Providing with caching"
        )
        assertNotEquals(
            windowUids,
            ListUtils.format(carInject.windows) { it: CarBox<Window> -> it.value.uuid },
            "Providing without caching"
        )
    }

    @Test
    fun carProtectProviderTest() {
        //Given
        val DI = Stone.createComponent(
            CarBoxedInjectComponent::class.java
        )

        //When
        var carInject: CarBoxedInjectProvider? = CarBoxedInjectProvider()
        DI.inject(carInject!!)
        val bumperUid = carInject.bumper!!.get().value.uuid
        val wheelUid = carInject.wheel!!.get().value.uuid
        val windowUid = carInject.window!!.get().value!!.value.uuid
        DI.protect(carInject)
        carInject = null
        System.gc()
        carInject = CarBoxedInjectProvider()
        DI.inject(carInject)


        // Then
        assertEquals(
            bumperUid,
            carInject.bumper!!.get().value.uuid,
            "Lazy Provider caching and can be protected"
        )
        assertNotEquals(
            wheelUid,
            carInject.wheel!!.get().value.uuid,
            "Ref is non caching and non should protect"
        )
        assertNotEquals(
            windowUid,
            carInject.window!!.get().value!!.value.uuid,
            "LazyProvide non support caching. Non should protect"
        )
    }
}
