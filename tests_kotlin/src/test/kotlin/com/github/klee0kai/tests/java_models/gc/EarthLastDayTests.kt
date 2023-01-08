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
        val mountainDef = WeakReference(di.earth().mountainDefaultSoft())
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
        val mountainDef = WeakReference(di.earth().mountainDefaultSoft())
        val riverStrong = WeakReference(di.earth().riverStrong())
        val riverSoft = WeakReference(di.earth().riverSoft())
        val riverWeak = WeakReference(di.earth().riverWeak())
        val riverDef = WeakReference(di.earth().riverDefaultSoft())

        //When
        di.gcStrong()

        //Then
        for (ref in Arrays.asList(
            mountainStrong, mountainWeak,
            riverStrong, riverWeak
        )) {
            assertNull(ref.get())
        }
        for (ref in Arrays.asList(
            mountainSoft, mountainDef,
            riverSoft, riverDef
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
        val mountainDef = WeakReference(di.earth().mountainDefaultSoft())
        val riverStrong = WeakReference(di.earth().riverStrong())
        val riverSoft = WeakReference(di.earth().riverSoft())
        val riverWeak = WeakReference(di.earth().riverWeak())
        val riverDef = WeakReference(di.earth().riverDefaultSoft())

        //When
        di.gcSoft()

        //Then
        for (ref in Arrays.asList(
            mountainSoft, mountainWeak, mountainDef,
            riverSoft, riverWeak, riverDef
        )) {
            assertNull(ref.get())
        }
        for (ref in Arrays.asList(
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
        val mountainDef = WeakReference(di.earth().mountainDefaultSoft())
        val riverStrong = WeakReference(di.earth().riverStrong())
        val riverSoft = WeakReference(di.earth().riverSoft())
        val riverWeak = WeakReference(di.earth().riverWeak())
        val riverDef = WeakReference(di.earth().riverDefaultSoft())

        //When
        di.gcWeak()

        //Then
        for (ref in Arrays.asList(
            mountainWeak,
            riverWeak
        )) {
            assertNull(ref.get())
        }
        for (ref in Arrays.asList(
            mountainStrong, mountainSoft, mountainDef,
            riverStrong, riverSoft, riverDef
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
        val mountainDef = WeakReference(di.earth().mountainDefaultSoft())
        val riverStrong = WeakReference(di.earth().riverStrong())
        val riverSoft = WeakReference(di.earth().riverSoft())
        val riverWeak = WeakReference(di.earth().riverWeak())
        val riverDef = WeakReference(di.earth().riverDefaultSoft())

        //When
        di.gcMountains()

        //Then
        for (ref in Arrays.asList(
            mountainStrong, mountainSoft, mountainWeak, mountainDef,
            riverWeak
        )) {
            assertNull(ref.get())
        }
        for (ref in Arrays.asList(
            riverStrong, riverSoft, riverDef
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
        val mountainDef = WeakReference(di.earth().mountainDefaultSoft())
        val riverStrong = WeakReference(di.earth().riverStrong())
        val riverSoft = WeakReference(di.earth().riverSoft())
        val riverWeak = WeakReference(di.earth().riverWeak())
        val riverDef = WeakReference(di.earth().riverDefaultSoft())

        //When
        di.gcRivers()

        //Then
        for (ref in Arrays.asList(
            mountainWeak,
            riverStrong, riverSoft, riverWeak, riverDef
        )) {
            assertNull(ref.get())
        }
        for (ref in Arrays.asList(
            mountainStrong, mountainSoft, mountainDef
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
        val mountainDef = WeakReference(di.earth().mountainDefaultSoft())
        val riverStrong = WeakReference(di.earth().riverStrong())
        val riverSoft = WeakReference(di.earth().riverSoft())
        val riverWeak = WeakReference(di.earth().riverWeak())
        val riverDef = WeakReference(di.earth().riverDefaultSoft())

        //When
        di.gcMountainsAndRivers()

        //Then
        for (ref in Arrays.asList(
            mountainStrong, mountainSoft, mountainWeak, mountainDef,
            riverStrong, riverSoft, riverWeak, riverDef
        )) {
            assertNull(ref.get())
        }
    }
}