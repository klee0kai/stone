package com.github.klee0kai.stone.test.car.cachecontrol.gc

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.car.di.cachecontrol.gc.CarGcComponent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class BumperGcResusableTests {
    @Test
    fun gcAllTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val bumperFactoryUids1 = DI.bumpersModule()!!.bumperFactory().map { it.uuid }
        val bumperWeakUids1 = DI.bumpersModule()!!.bumperWeak().map { it.uuid }
        val bumperSoftUids1 = DI.bumpersModule()!!.bumperSoft().map { it.uuid }
        val bumperStrongUids1 = DI.bumpersModule()!!.bumperStrong().map { it.uuid }

        //When
        DI.gcAll()
        val bumperFactoryUids2 = DI.bumpersModule()!!.bumperFactory().map { it.uuid }
        val bumperWeakUids2 = DI.bumpersModule()!!.bumperWeak().map { it.uuid }
        val bumperSoftUids2 = DI.bumpersModule()!!.bumperSoft().map { it.uuid }
        val bumperStrongUids2 = DI.bumpersModule()!!.bumperStrong().map { it.uuid }

        // Then
        assertNotEquals(bumperFactoryUids1, bumperFactoryUids2)
        assertNotEquals(bumperWeakUids1, bumperWeakUids2)
        assertNotEquals(bumperSoftUids1, bumperSoftUids2)
        assertNotEquals(bumperStrongUids1, bumperStrongUids2)
    }

    @Test
    fun gcWeakTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val bumperFactoryUids1 = DI.bumpersModule()!!.bumperFactory().map { it.uuid }
        val bumperWeakUids1 = DI.bumpersModule()!!.bumperWeak().map { it.uuid }
        val bumperSoftUids1 = DI.bumpersModule()!!.bumperSoft().map { it.uuid }
        val bumperStrongUids1 = DI.bumpersModule()!!.bumperStrong().map { it.uuid }

        //When
        DI.gcWeak()
        val bumperFactoryUids2 = DI.bumpersModule()!!.bumperFactory().map { it.uuid }
        val bumperWeakUids2 = DI.bumpersModule()!!.bumperWeak().map { it.uuid }
        val bumperSoftUids2 = DI.bumpersModule()!!.bumperSoft().map { it.uuid }
        val bumperStrongUids2 = DI.bumpersModule()!!.bumperStrong().map { it.uuid }


        // Then
        assertNotEquals(bumperFactoryUids1, bumperFactoryUids2)
        assertNotEquals(bumperWeakUids1, bumperWeakUids2)
        assertEquals(bumperSoftUids1, bumperSoftUids2)
        assertEquals(bumperStrongUids1, bumperStrongUids2)
    }

    @Test
    fun gcSoftTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val bumperFactoryUids1 = DI.bumpersModule()!!.bumperFactory().map { it.uuid }
        val bumperWeakUids1 = DI.bumpersModule()!!.bumperWeak().map { it.uuid }
        val bumperSoftUids1 = DI.bumpersModule()!!.bumperSoft().map { it.uuid }
        val bumperStrongUids1 = DI.bumpersModule()!!.bumperStrong().map { it.uuid }

        //When
        DI.gcSoft()
        val bumperFactoryUids2 = DI.bumpersModule()!!.bumperFactory().map { it.uuid }
        val bumperWeakUids2 = DI.bumpersModule()!!.bumperWeak().map { it.uuid }
        val bumperSoftUids2 = DI.bumpersModule()!!.bumperSoft().map { it.uuid }
        val bumperStrongUids2 = DI.bumpersModule()!!.bumperStrong().map { it.uuid }

        // Then
        assertNotEquals(bumperFactoryUids1, bumperFactoryUids2)
        assertNotEquals(bumperWeakUids1, bumperWeakUids2)
        assertNotEquals(bumperSoftUids1, bumperSoftUids2)
        assertEquals(bumperStrongUids1, bumperStrongUids2)
    }

    @Test
    fun gcStrongTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val bumperFactoryUids1 = DI.bumpersModule()!!.bumperFactory().map { it.uuid }
        val bumperWeakUids1 = DI.bumpersModule()!!.bumperWeak().map { it.uuid }
        val bumperSoftUids1 = DI.bumpersModule()!!.bumperSoft().map { it.uuid }
        val bumperStrongUids1 = DI.bumpersModule()!!.bumperStrong().map { it.uuid }

        //When
        DI.gcStrong()
        val bumperFactoryUids2 = DI.bumpersModule()!!.bumperFactory().map { it.uuid }
        val bumperWeakUids2 = DI.bumpersModule()!!.bumperWeak().map { it.uuid }
        val bumperSoftUids2 = DI.bumpersModule()!!.bumperSoft().map { it.uuid }
        val bumperStrongUids2 = DI.bumpersModule()!!.bumperStrong().map { it.uuid }

        // Then
        assertNotEquals(bumperFactoryUids1, bumperFactoryUids2)
        assertNotEquals(bumperWeakUids1, bumperWeakUids2)
        assertEquals(bumperSoftUids1, bumperSoftUids2)
        assertNotEquals(bumperStrongUids1, bumperStrongUids2)
    }

    @Test
    fun gcBumpers() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val bumperFactoryUids1 = DI.bumpersModule()!!.bumperFactory().map { it.uuid }
        val bumperWeakUids1 = DI.bumpersModule()!!.bumperWeak().map { it.uuid }
        val bumperSoftUids1 = DI.bumpersModule()!!.bumperSoft().map { it.uuid }
        val bumperStrongUids1 = DI.bumpersModule()!!.bumperStrong().map { it.uuid }

        //When
        DI.gcBumpers()
        val bumperFactoryUids2 = DI.bumpersModule()!!.bumperFactory().map { it.uuid }
        val bumperWeakUids2 = DI.bumpersModule()!!.bumperWeak().map { it.uuid }
        val bumperSoftUids2 = DI.bumpersModule()!!.bumperSoft().map { it.uuid }
        val bumperStrongUids2 = DI.bumpersModule()!!.bumperStrong().map { it.uuid }

        // Then
        assertNotEquals(bumperFactoryUids1, bumperFactoryUids2)
        assertNotEquals(bumperWeakUids1, bumperWeakUids2)
        assertNotEquals(bumperSoftUids1, bumperSoftUids2)
        assertNotEquals(bumperStrongUids1, bumperStrongUids2)
    }

    @Test
    fun gcRedBumpers() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val bumperFactoryUids1 = DI.bumpersModule()!!.bumperFactory().map { it.uuid }
        val bumperWeakUids1 = DI.bumpersModule()!!.bumperWeak().map { it.uuid }
        val bumperSoftUids1 = DI.bumpersModule()!!.bumperSoft().map { it.uuid }
        val bumperStrongUids1 = DI.bumpersModule()!!.bumperStrong().map { it.uuid }

        //When
        DI.gcRedBumpers()
        val bumperFactoryUids2 = DI.bumpersModule()!!.bumperFactory().map { it.uuid }
        val bumperWeakUids2 = DI.bumpersModule()!!.bumperWeak().map { it.uuid }
        val bumperSoftUids2 = DI.bumpersModule()!!.bumperSoft().map { it.uuid }
        val bumperStrongUids2 = DI.bumpersModule()!!.bumperStrong().map { it.uuid }

        // Then
        assertNotEquals(bumperFactoryUids1, bumperFactoryUids2)
        assertNotEquals(bumperWeakUids1, bumperWeakUids2)
        assertEquals(bumperSoftUids1, bumperSoftUids2)
        assertNotEquals(bumperStrongUids1, bumperStrongUids2)
    }

    @Test
    fun gcRedBumpers2() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val bumperFactoryUids1 = DI.bumpersModule()!!.bumperFactory().map { it.uuid }
        val bumperWeakUids1 = DI.bumpersModule()!!.bumperWeak().map { it.uuid }
        val bumperSoftUids1 = DI.bumpersModule()!!.bumperSoft().map { it.uuid }
        val bumperStrongUids1 = DI.bumpersModule()!!.bumperStrong().map { it.uuid }

        //When
        DI.gcRedBumpers2()
        val bumperFactoryUids2 = DI.bumpersModule()!!.bumperFactory().map { it.uuid }
        val bumperWeakUids2 = DI.bumpersModule()!!.bumperWeak().map { it.uuid }
        val bumperSoftUids2 = DI.bumpersModule()!!.bumperSoft().map { it.uuid }
        val bumperStrongUids2 = DI.bumpersModule()!!.bumperStrong().map { it.uuid }

        // Then
        assertNotEquals(bumperFactoryUids1, bumperFactoryUids2)
        assertNotEquals(bumperWeakUids1, bumperWeakUids2)
        assertEquals(bumperSoftUids1, bumperSoftUids2)
        assertNotEquals(bumperStrongUids1, bumperStrongUids2)
    }

    @Test
    fun gcWheelsTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val bumperFactoryUids1 = DI.bumpersModule()!!.bumperFactory().map { it.uuid }
        val bumperWeakUids1 = DI.bumpersModule()!!.bumperWeak().map { it.uuid }
        val bumperSoftUids1 = DI.bumpersModule()!!.bumperSoft().map { it.uuid }
        val bumperStrongUids1 = DI.bumpersModule()!!.bumperStrong().map { it.uuid }

        //When
        DI.gcWheels()
        val bumperFactoryUids2 = DI.bumpersModule()!!.bumperFactory().map { it.uuid }
        val bumperWeakUids2 = DI.bumpersModule()!!.bumperWeak().map { it.uuid }
        val bumperSoftUids2 = DI.bumpersModule()!!.bumperSoft().map { it.uuid }
        val bumperStrongUids2 = DI.bumpersModule()!!.bumperStrong().map { it.uuid }

        // Then
        assertNotEquals(bumperFactoryUids1, bumperFactoryUids2)
        assertNotEquals(bumperWeakUids1, bumperWeakUids2)
        assertEquals(bumperSoftUids1, bumperSoftUids2)
        assertEquals(bumperStrongUids1, bumperStrongUids2)
    }

    @Test
    fun gcNothing() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val bumperFactoryUids1 = DI.bumpersModule()!!.bumperFactory().map { it.uuid }
        val bumperWeakUids1 = DI.bumpersModule()!!.bumperWeak().map { it.uuid }
        val bumperSoftUids1 = DI.bumpersModule()!!.bumperSoft().map { it.uuid }
        val bumperStrongUids1 = DI.bumpersModule()!!.bumperStrong().map { it.uuid }

        //When
        DI.gcNothing()
        val bumperFactoryUids2 = DI.bumpersModule()!!.bumperFactory().map { it.uuid }
        val bumperWeakUids2 = DI.bumpersModule()!!.bumperWeak().map { it.uuid }
        val bumperSoftUids2 = DI.bumpersModule()!!.bumperSoft().map { it.uuid }
        val bumperStrongUids2 = DI.bumpersModule()!!.bumperStrong().map { it.uuid }

        // Then
        assertNotEquals(bumperFactoryUids1, bumperFactoryUids2)
        assertNotEquals(bumperWeakUids1, bumperWeakUids2)
        assertEquals(bumperSoftUids1, bumperSoftUids2)
        assertEquals(bumperStrongUids1, bumperStrongUids2)
    }
}
