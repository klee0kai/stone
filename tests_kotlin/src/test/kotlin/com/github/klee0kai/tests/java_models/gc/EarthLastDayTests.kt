package com.github.klee0kai.tests.java_models.gc

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.di.gcforest.GcGodComponent
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.lang.ref.WeakReference
import java.util.*

class EarthLastDayTests {
    @Test
    fun gcAllTest() {
        //Given
        val di = Stone.createComponent(GcGodComponent::class.java)
        val mountainStrong = WeakReference(di.earth().mountainStrong())
        val mountainSoft = WeakReference(di.earth().mountainSoft())
        val mountainWeak = WeakReference(di.earth().mountainWeak())
        val mountainDef = WeakReference(di.earth().mountainDefaultFactory())
        val riverStrong = WeakReference(di.earth().riverStrong())
        val riverSoft = WeakReference(di.earth().riverSoft())
        val riverWeak = WeakReference(di.earth().riverWeak())
        val riverDef = WeakReference(di.earth().riverDefaultSoft())

        //When
        di.gcAll()

        //Then
        for (ref in Arrays.asList(
            mountainStrong, mountainSoft, mountainWeak, mountainDef,
            riverStrong, riverSoft, riverWeak, riverDef
        )) {
            assertNull(ref.get())
        }
    }

    @Test
    fun gcStrongTest() {
        //Given
        val di = Stone.createComponent(GcGodComponent::class.java)
        val mountainStrong = WeakReference(di.earth().mountainStrong())
        val mountainSoft = WeakReference(di.earth().mountainSoft())
        val mountainWeak = WeakReference(di.earth().mountainWeak())
        val mountainDef = WeakReference(di.earth().mountainDefaultFactory())
        val riverStrong = WeakReference(di.earth().riverStrong())
        val riverSoft = WeakReference(di.earth().riverSoft())
        val riverWeak = WeakReference(di.earth().riverWeak())
        val riverDef = WeakReference(di.earth().riverDefaultSoft())

        //When
        di.gcStrong()

        //Then
        for (ref in listOf(
            mountainStrong, mountainWeak, mountainDef,
            riverStrong, riverWeak, riverDef
        )) {
            assertNull(ref.get())
        }
        for (ref in listOf(
            mountainSoft,
            riverSoft,
        )) {
            assertNotNull(ref.get())
        }
    }

    @Test
    fun gcSoftTest() {
        //Given
        val di = Stone.createComponent(GcGodComponent::class.java)
        val mountainStrong = WeakReference(di.earth().mountainStrong())
        val mountainSoft = WeakReference(di.earth().mountainSoft())
        val mountainWeak = WeakReference(di.earth().mountainWeak())
        val mountainDef = WeakReference(di.earth().mountainDefaultFactory())
        val riverStrong = WeakReference(di.earth().riverStrong())
        val riverSoft = WeakReference(di.earth().riverSoft())
        val riverWeak = WeakReference(di.earth().riverWeak())
        val riverDef = WeakReference(di.earth().riverDefaultSoft())

        //When
        di.gcSoft()

        //Then
        for (ref in listOf(
            mountainSoft, mountainWeak, mountainDef,
            riverSoft, riverWeak, riverDef
        )) {
            assertNull(ref.get())
        }
        for (ref in listOf(
            mountainStrong,
            riverStrong
        )) {
            assertNotNull(ref.get())
        }
    }

    @Test
    fun gcWeakTest() {
        //Given
        val di = Stone.createComponent(GcGodComponent::class.java)
        val mountainStrong = WeakReference(di.earth().mountainStrong())
        val mountainSoft = WeakReference(di.earth().mountainSoft())
        val mountainWeak = WeakReference(di.earth().mountainWeak())
        val mountainDef = WeakReference(di.earth().mountainDefaultFactory())
        val riverStrong = WeakReference(di.earth().riverStrong())
        val riverSoft = WeakReference(di.earth().riverSoft())
        val riverWeak = WeakReference(di.earth().riverWeak())
        val riverDef = WeakReference(di.earth().riverDefaultSoft())

        //When
        di.gcWeak()

        //Then
        for (ref in listOf(
            mountainWeak, mountainDef,
            riverWeak, riverDef
        )) {
            assertNull(ref.get())
        }
        for (ref in listOf(
            mountainStrong, mountainSoft,
            riverStrong, riverSoft
        )) {
            assertNotNull(ref.get())
        }
    }

    @Test
    fun gcMountainTest() {
        //Given
        val di = Stone.createComponent(GcGodComponent::class.java)
        val mountainStrong = WeakReference(di.earth().mountainStrong())
        val mountainSoft = WeakReference(di.earth().mountainSoft())
        val mountainWeak = WeakReference(di.earth().mountainWeak())
        val mountainDef = WeakReference(di.earth().mountainDefaultFactory())
        val riverStrong = WeakReference(di.earth().riverStrong())
        val riverSoft = WeakReference(di.earth().riverSoft())
        val riverWeak = WeakReference(di.earth().riverWeak())
        val riverDef = WeakReference(di.earth().riverDefaultSoft())

        //When
        di.gcMountains()

        //Then
        for (ref in listOf(
            mountainStrong, mountainSoft, mountainWeak, mountainDef,
            riverWeak, riverDef
        )) {
            assertNull(ref.get())
        }
        for (ref in listOf(
            riverStrong, riverSoft,
        )) {
            assertNotNull(ref.get())
        }
    }

    @Test
    fun gcRiverTest() {
        //Given
        val di = Stone.createComponent(GcGodComponent::class.java)
        val mountainStrong = WeakReference(di.earth().mountainStrong())
        val mountainSoft = WeakReference(di.earth().mountainSoft())
        val mountainWeak = WeakReference(di.earth().mountainWeak())
        val mountainDef = WeakReference(di.earth().mountainDefaultFactory())
        val riverStrong = WeakReference(di.earth().riverStrong())
        val riverSoft = WeakReference(di.earth().riverSoft())
        val riverWeak = WeakReference(di.earth().riverWeak())
        val riverDef = WeakReference(di.earth().riverDefaultSoft())

        //When
        di.gcRivers()

        //Then
        for (ref in listOf(
            mountainWeak, mountainDef,
            riverStrong, riverSoft, riverWeak, riverDef
        )) {
            assertNull(ref.get())
        }
        for (ref in listOf(
            mountainStrong, mountainSoft,
        )) {
            assertNotNull(ref.get())
        }
    }

    @Test
    fun gcMountainAndRiverTest() {
        //Given
        val di = Stone.createComponent(GcGodComponent::class.java)
        val mountainStrong = WeakReference(di.earth().mountainStrong())
        val mountainSoft = WeakReference(di.earth().mountainSoft())
        val mountainWeak = WeakReference(di.earth().mountainWeak())
        val mountainDef = WeakReference(di.earth().mountainDefaultFactory())
        val riverStrong = WeakReference(di.earth().riverStrong())
        val riverSoft = WeakReference(di.earth().riverSoft())
        val riverWeak = WeakReference(di.earth().riverWeak())
        val riverDef = WeakReference(di.earth().riverDefaultSoft())

        //When
        di.gcMountainsAndRivers()

        //Then
        for (ref in listOf(
            mountainStrong, mountainSoft, mountainWeak, mountainDef,
            riverStrong, riverSoft, riverWeak, riverDef
        )) {
            assertNull(ref.get())
        }
    }

    @Test
    fun gcSoftMountainTest() {
        //Given
        val di = Stone.createComponent(GcGodComponent::class.java)
        val mountainStrong = WeakReference(di.earth().mountainStrong())
        val mountainSoft = WeakReference(di.earth().mountainSoft())
        val mountainWeak = WeakReference(di.earth().mountainWeak())
        val mountainDef = WeakReference(di.earth().mountainDefaultFactory())
        val riverStrong = WeakReference(di.earth().riverStrong())
        val riverSoft = WeakReference(di.earth().riverSoft())
        val riverWeak = WeakReference(di.earth().riverWeak())
        val riverDef = WeakReference(di.earth().riverDefaultSoft())

        //When
        di.gcSoftMountains()

        //Then
        for (ref in listOf(
            mountainSoft, mountainWeak, mountainDef,
            riverWeak, riverDef
        )) {
            assertNull(ref.get())
        }
        for (ref in listOf(
            mountainStrong,
            riverStrong, riverSoft,
        )) {
            assertNotNull(ref.get())
        }
    }

    @Test
    fun gcStrongMountainTest() {
        //Given
        val di = Stone.createComponent(GcGodComponent::class.java)
        val mountainStrong = WeakReference(di.earth().mountainStrong())
        val mountainSoft = WeakReference(di.earth().mountainSoft())
        val mountainWeak = WeakReference(di.earth().mountainWeak())
        val mountainDef = WeakReference(di.earth().mountainDefaultFactory())
        val riverStrong = WeakReference(di.earth().riverStrong())
        val riverSoft = WeakReference(di.earth().riverSoft())
        val riverWeak = WeakReference(di.earth().riverWeak())
        val riverDef = WeakReference(di.earth().riverDefaultSoft())

        //When
        di.gcStrongMountains()

        //Then
        for (ref in listOf(
            mountainStrong, mountainWeak, mountainDef,
            riverWeak, riverDef
        )) {
            assertNull(ref.get())
        }
        for (ref in listOf(
            mountainSoft,
            riverStrong, riverSoft,
        )) {
            assertNotNull(ref.get())
        }
    }
}