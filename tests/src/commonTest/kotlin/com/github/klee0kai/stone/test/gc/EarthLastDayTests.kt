package com.github.klee0kai.stone.test.gc

import com.github.klee0kai.stone.weakref.WeakRef
import com.github.klee0kai.test.di.gcforest.GcGodComponentStoneComponent
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import java.lang.ref.WeakReference
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class EarthLastDayTests {

    @Test
    fun gcAllTest() {
        //Given
        val di = GcGodComponentStoneComponent()
        val mountainStrong = WeakRef(di.earth().mountainStrong())
        val mountainSoft = WeakRef(di.earth().mountainSoft())
        val mountainWeak = WeakRef(di.earth().mountainWeak())
        val mountainDef = WeakRef(di.earth().mountainDefaultFactory())
        val mountainDef2 = WeakRef(di.earth().mountainDefault2Factory())
        val riverStrong = WeakRef(di.earth().riverStrong())
        val riverSoft = WeakRef(di.earth().riverSoft())
        val riverWeak = WeakRef(di.earth().riverWeak())
        val riverDef = WeakRef(di.earth().riverDefaultSoft())

        //When
        di.gcAll()

        //Then
        for (ref in listOf(
            mountainStrong, mountainSoft, mountainDef2, mountainWeak, mountainDef,
            riverStrong, riverSoft, riverWeak, riverDef
        )) {
            assertNull(ref.get())
        }
    }

    @Test
    fun gcStrongTest() {
        //Given
        val di = GcGodComponentStoneComponent()
        val mountainStrong = WeakRef(di.earth().mountainStrong())
        val mountainSoft = WeakRef(di.earth().mountainSoft())
        val mountainWeak = WeakRef(di.earth().mountainWeak())
        val mountainDef = WeakRef(di.earth().mountainDefaultFactory())
        val mountainDef2 = WeakRef(di.earth().mountainDefault2Factory())
        val riverStrong = WeakRef(di.earth().riverStrong())
        val riverSoft = WeakRef(di.earth().riverSoft())
        val riverWeak = WeakRef(di.earth().riverWeak())
        val riverDef = WeakRef(di.earth().riverDefaultSoft())

        //When
        di.gcStrong()

        //Then
        for (ref in listOf(
            mountainStrong, mountainWeak, mountainDef, mountainDef2,
            riverStrong, riverWeak, riverDef
        )) {
            assertNull(ref.get())
        }

        for (ref in listOf(
            mountainSoft,
            riverSoft
        )) {
            assertNotNull(ref.get())
        }
    }

    @Test
    fun gcSoftTest() {
        //Given
        val di = GcGodComponentStoneComponent()
        val mountainStrong = WeakRef(di.earth().mountainStrong())
        val mountainSoft = WeakRef(di.earth().mountainSoft())
        val mountainWeak = WeakRef(di.earth().mountainWeak())
        val mountainDef = WeakRef(di.earth().mountainDefaultFactory())
        val mountainDef2 = WeakRef(di.earth().mountainDefault2Factory())
        val riverStrong = WeakRef(di.earth().riverStrong())
        val riverSoft = WeakRef(di.earth().riverSoft())
        val riverWeak = WeakRef(di.earth().riverWeak())
        val riverDef = WeakRef(di.earth().riverDefaultSoft())

        //When
        di.gcSoft()

        //Then
        for (ref in listOf(
            mountainSoft, mountainWeak, mountainDef, mountainDef2,
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
        val di = GcGodComponentStoneComponent()
        val mountainStrong = WeakRef(di.earth().mountainStrong())
        val mountainSoft = WeakRef(di.earth().mountainSoft())
        val mountainWeak = WeakRef(di.earth().mountainWeak())
        val mountainDef = WeakRef(di.earth().mountainDefaultFactory())
        val riverStrong = WeakRef(di.earth().riverStrong())
        val riverSoft = WeakRef(di.earth().riverSoft())
        val riverWeak = WeakRef(di.earth().riverWeak())
        val riverDef = WeakRef(di.earth().riverDefaultSoft())

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
        val di = GcGodComponentStoneComponent()
        val mountainStrong = WeakRef(di.earth().mountainStrong())
        val mountainSoft = WeakRef(di.earth().mountainSoft())
        val mountainWeak = WeakRef(di.earth().mountainWeak())
        val mountainDef = WeakRef(di.earth().mountainDefaultFactory())
        val riverStrong = WeakRef(di.earth().riverStrong())
        val riverSoft = WeakRef(di.earth().riverSoft())
        val riverWeak = WeakRef(di.earth().riverWeak())
        val riverDef = WeakRef(di.earth().riverDefaultSoft())

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
            riverStrong, riverSoft
        )) {
            assertNotNull(ref.get())
        }
    }

    @Test
    fun gcRiverTest() {
        //Given
        val di = GcGodComponentStoneComponent()
        val mountainStrong = WeakRef(di.earth().mountainStrong())
        val mountainSoft = WeakRef(di.earth().mountainSoft())
        val mountainWeak = WeakRef(di.earth().mountainWeak())
        val mountainDef = WeakRef(di.earth().mountainDefaultFactory())
        val riverStrong = WeakRef(di.earth().riverStrong())
        val riverSoft = WeakRef(di.earth().riverSoft())
        val riverWeak = WeakRef(di.earth().riverWeak())
        val riverDef = WeakRef(di.earth().riverDefaultSoft())

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
            mountainStrong, mountainSoft
        )) {
            assertNotNull(ref.get())
        }
    }

    @Test
    fun gcMountainAndRiverTest() {
        //Given
        val di = GcGodComponentStoneComponent()
        val mountainStrong = WeakRef(di.earth().mountainStrong())
        val mountainSoft = WeakRef(di.earth().mountainSoft())
        val mountainWeak = WeakRef(di.earth().mountainWeak())
        val mountainDef = WeakRef(di.earth().mountainDefaultFactory())
        val riverStrong = WeakRef(di.earth().riverStrong())
        val riverSoft = WeakRef(di.earth().riverSoft())
        val riverWeak = WeakRef(di.earth().riverWeak())
        val riverDef = WeakRef(di.earth().riverDefaultSoft())

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
        val di = GcGodComponentStoneComponent()
        val mountainStrong = WeakRef(di.earth().mountainStrong())
        val mountainSoft = WeakRef(di.earth().mountainSoft())
        val mountainWeak = WeakRef(di.earth().mountainWeak())
        val mountainDef = WeakRef(di.earth().mountainDefaultFactory())
        val riverStrong = WeakRef(di.earth().riverStrong())
        val riverSoft = WeakRef(di.earth().riverSoft())
        val riverWeak = WeakRef(di.earth().riverWeak())
        val riverDef = WeakRef(di.earth().riverDefaultSoft())

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
            riverStrong, riverSoft
        )) {
            assertNotNull(ref.get())
        }
    }

    @Test
    fun gcStrongMountainTest() {
        //Given
        val di = GcGodComponentStoneComponent()
        val mountainStrong = WeakRef(di.earth().mountainStrong())
        val mountainSoft = WeakRef(di.earth().mountainSoft())
        val mountainWeak = WeakRef(di.earth().mountainWeak())
        val mountainDef = WeakRef(di.earth().mountainDefaultFactory())
        val riverStrong = WeakRef(di.earth().riverStrong())
        val riverSoft = WeakRef(di.earth().riverSoft())
        val riverWeak = WeakRef(di.earth().riverWeak())
        val riverDef = WeakRef(di.earth().riverDefaultSoft())

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
            riverStrong, riverSoft
        )) {
            assertNotNull(ref.get())
        }
    }

}
