package com.github.klee0kai.stone.test.car.cachecontrol.gc

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.car.di.cachecontrol.gc.CarGcComponent
import com.github.klee0kai.test.car.model.Bumper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.lang.ref.WeakReference

class BumperGcTests {
    @Test
    fun createWorkCorrect() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)

        //When
        val bumperFactory = DI.bumpersModule()!!.bumperFactory().map { WeakReference(it) }
        val bumperWeak = DI.bumpersModule()!!.bumperWeak().map { WeakReference(it) }
        val bumperSoft = DI.bumpersModule()!!.bumperSoft().map { WeakReference(it) }
        val bumperStrong = DI.bumpersModule()!!.bumperStrong().map { WeakReference(it) }

        //Then
        assertEquals(3, nonNullCount(bumperFactory))
        assertEquals(3, nonNullCount(bumperWeak))
        assertEquals(3, nonNullCount(bumperSoft))
        assertEquals(3, nonNullCount(bumperStrong))
    }

    @Test
    fun gcAllTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val bumperFactory = DI.bumpersModule()!!.bumperFactory().map { WeakReference(it) }
        val bumperWeak = DI.bumpersModule()!!.bumperWeak().map { WeakReference(it) }
        val bumperSoft = DI.bumpersModule()!!.bumperSoft().map { WeakReference(it) }
        val bumperStrong = DI.bumpersModule()!!.bumperStrong().map { WeakReference(it) }

        //When
        DI.gcAll()

        // Then
        assertEquals(0, nonNullCount(bumperFactory))
        assertEquals(0, nonNullCount(bumperWeak))
        assertEquals(0, nonNullCount(bumperSoft))
        assertEquals(0, nonNullCount(bumperStrong))
    }

    @Test
    fun gcWeakTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val bumperFactory = DI.bumpersModule()!!.bumperFactory().map { WeakReference(it) }
        val bumperWeak = DI.bumpersModule()!!.bumperWeak().map { WeakReference(it) }
        val bumperSoft = DI.bumpersModule()!!.bumperSoft().map { WeakReference(it) }
        val bumperStrong = DI.bumpersModule()!!.bumperStrong().map { WeakReference(it) }

        //When
        DI.gcWeak()

        // Then
        assertEquals(0, nonNullCount(bumperFactory))
        assertEquals(0, nonNullCount(bumperWeak))
        assertEquals(3, nonNullCount(bumperSoft))
        assertEquals(3, nonNullCount(bumperStrong))
    }

    @Test
    fun gcSoftTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val bumperFactory = DI.bumpersModule()!!.bumperFactory().map { WeakReference(it) }
        val bumperWeak = DI.bumpersModule()!!.bumperWeak().map { WeakReference(it) }
        val bumperSoft = DI.bumpersModule()!!.bumperSoft().map { WeakReference(it) }
        val bumperStrong = DI.bumpersModule()!!.bumperStrong().map { WeakReference(it) }

        //When
        DI.gcSoft()

        // Then
        assertEquals(0, nonNullCount(bumperFactory))
        assertEquals(0, nonNullCount(bumperWeak))
        assertEquals(0, nonNullCount(bumperSoft))
        assertEquals(3, nonNullCount(bumperStrong))
    }

    @Test
    fun gcStrongTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val bumperFactory = DI.bumpersModule()!!.bumperFactory().map { WeakReference(it) }
        val bumperWeak = DI.bumpersModule()!!.bumperWeak().map { WeakReference(it) }
        val bumperSoft = DI.bumpersModule()!!.bumperSoft().map { WeakReference(it) }
        val bumperStrong = DI.bumpersModule()!!.bumperStrong().map { WeakReference(it) }

        //When
        DI.gcStrong()

        // Then
        assertEquals(0, nonNullCount(bumperFactory))
        assertEquals(0, nonNullCount(bumperWeak))
        assertEquals(3, nonNullCount(bumperSoft))
        assertEquals(0, nonNullCount(bumperStrong))
    }

    @Test
    fun gcBumpers() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val bumperFactory = DI.bumpersModule()!!.bumperFactory().map { WeakReference(it) }
        val bumperWeak = DI.bumpersModule()!!.bumperWeak().map { WeakReference(it) }
        val bumperSoft = DI.bumpersModule()!!.bumperSoft().map { WeakReference(it) }
        val bumperStrong = DI.bumpersModule()!!.bumperStrong().map { WeakReference(it) }

        //When
        DI.gcBumpers()

        // Then
        assertEquals(0, nonNullCount(bumperFactory))
        assertEquals(0, nonNullCount(bumperWeak))
        assertEquals(0, nonNullCount(bumperSoft))
        assertEquals(0, nonNullCount(bumperStrong))
    }

    @Test
    fun gcRedBumpers() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val bumperFactory = DI.bumpersModule()!!.bumperFactory().map { WeakReference(it) }
        val bumperWeak = DI.bumpersModule()!!.bumperWeak().map { WeakReference(it) }
        val bumperSoft = DI.bumpersModule()!!.bumperSoft().map { WeakReference(it) }
        val bumperStrong = DI.bumpersModule()!!.bumperStrong().map { WeakReference(it) }

        //When
        DI.gcRedBumpers()

        // Then
        assertEquals(0, nonNullCount(bumperFactory))
        assertEquals(0, nonNullCount(bumperWeak))
        assertEquals(3, nonNullCount(bumperSoft))
        assertEquals(0, nonNullCount(bumperStrong))
    }

    @Test
    fun gcRedBumpers2() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val bumperFactory = DI.bumpersModule()!!.bumperFactory().map { WeakReference(it) }
        val bumperWeak = DI.bumpersModule()!!.bumperWeak().map { WeakReference(it) }
        val bumperSoft = DI.bumpersModule()!!.bumperSoft().map { WeakReference(it) }
        val bumperStrong = DI.bumpersModule()!!.bumperStrong().map { WeakReference(it) }

        //When
        DI.gcRedBumpers2()

        // Then
        assertEquals(0, nonNullCount(bumperFactory))
        assertEquals(0, nonNullCount(bumperWeak))
        assertEquals(3, nonNullCount(bumperSoft))
        assertEquals(0, nonNullCount(bumperStrong))
    }

    @Test
    fun gcWheelsTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val bumperFactory = DI.bumpersModule()!!.bumperFactory().map { WeakReference(it) }
        val bumperWeak = DI.bumpersModule()!!.bumperWeak().map { WeakReference(it) }
        val bumperSoft = DI.bumpersModule()!!.bumperSoft().map { WeakReference(it) }
        val bumperStrong = DI.bumpersModule()!!.bumperStrong().map { WeakReference(it) }


        //When
        DI.gcWheels()

        // Then
        assertEquals(0, nonNullCount(bumperFactory))
        assertEquals(0, nonNullCount(bumperWeak))
        assertEquals(3, nonNullCount(bumperSoft))
        assertEquals(3, nonNullCount(bumperStrong))
    }

    @Test
    fun gcNothing() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val bumperFactory = DI.bumpersModule()!!.bumperFactory().map { WeakReference(it) }
        val bumperWeak = DI.bumpersModule()!!.bumperWeak().map { WeakReference(it) }
        val bumperSoft = DI.bumpersModule()!!.bumperSoft().map { WeakReference(it) }
        val bumperStrong = DI.bumpersModule()!!.bumperStrong().map { WeakReference(it) }

        //When
        DI.gcNothing()

        // Then
        assertEquals(0, nonNullCount(bumperFactory))
        assertEquals(0, nonNullCount(bumperWeak))
        assertEquals(3, nonNullCount(bumperSoft))
        assertEquals(3, nonNullCount(bumperStrong))
    }

    private fun nonNullCount(list: List<WeakReference<Bumper?>?>): Int = list.count { it?.get() != null }

}
