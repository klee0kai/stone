package com.dirgub.klee0kai.stone.text_ext.gc

import com.github.klee0kai.test.di.gcforest.GcGodComponent
import com.github.klee0kai.test.di.gcforest.GcGodComponentStoneComponent
import com.github.klee0kai.test.mowgli.earth.Mountain
import com.github.klee0kai.test.mowgli.earth.River
import com.github.klee0kai.test_ext.inject.di.gcforest.GcGodExtComponent
import com.github.klee0kai.test_ext.inject.di.gcforest.GcGodExtComponentStoneComponent
import com.github.klee0kai.test_ext.inject.mowgli.earth.Desert
import com.github.klee0kai.test_ext.inject.mowgli.earth.WaterFlow
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import java.lang.ref.WeakReference

class EarthLastDayAndProTests {

    @Test
    fun gcAllTest() {
        //Given
        val DI: GcGodComponent = GcGodComponentStoneComponent()
        val mountainStrong = WeakReference<Mountain?>(DI.earth()?.mountainStrong())
        val mountainSoft = WeakReference<Mountain?>(DI.earth()!!.mountainSoft())
        val mountainWeak = WeakReference<Mountain?>(DI.earth()!!.mountainWeak())
        val mountainDef = WeakReference<Mountain?>(DI.earth()!!.mountainDefaultFactory())
        val mountainDef2 = WeakReference<Mountain?>(DI.earth()!!.mountainDefault2Factory())
        val riverStrong = WeakReference<River?>(DI.earth()!!.riverStrong())
        val riverSoft = WeakReference<River?>(DI.earth()!!.riverSoft())
        val riverWeak = WeakReference<River?>(DI.earth()!!.riverWeak())
        val riverDef = WeakReference<River?>(DI.earth()!!.riverDefaultSoft())
        val DIPro: GcGodExtComponent = GcGodExtComponentStoneComponent()
        DIPro.extOf(DI)
        val desertStrong = WeakReference<Desert?>(DIPro.earth()!!.desertStrong())
        val desertSoft = WeakReference<Desert?>(DIPro.earth()!!.desertSoft())
        val desertWeak = WeakReference<Desert?>(DIPro.earth()!!.desertWeak())
        val desertFactory = WeakReference(DIPro.earth()!!.desertFactory())
        val waterFlowStrong = WeakReference<WaterFlow?>(DIPro.earth()!!.riverStrong())
        val waterFlowSoft = WeakReference<WaterFlow?>(DIPro.earth()!!.riverSoft())
        val waterFlowWeak = WeakReference<WaterFlow?>(DIPro.earth()!!.riverWeak())
        val waterFlowDefRef = WeakReference<WaterFlow?>(DIPro.earth()!!.riverDefaultSoft())

        //When
        DI.gcAll()

        //Then
        for (ref in listOf(
            mountainStrong, mountainSoft, mountainDef2, mountainWeak, mountainDef,
            riverStrong, riverSoft, riverWeak, riverDef,
            desertStrong, desertSoft, desertWeak, desertFactory,
            waterFlowStrong, waterFlowSoft, waterFlowWeak, waterFlowDefRef
        )) {
            assertNull(ref.get())
        }
    }

    @Test
    fun gcStrongTest() {
        //Given
        val DI: GcGodComponent = GcGodComponentStoneComponent()
        val mountainStrong = WeakReference<Mountain?>(DI.earth()?.mountainStrong())
        val mountainSoft = WeakReference<Mountain?>(DI.earth()!!.mountainSoft())
        val mountainWeak = WeakReference<Mountain?>(DI.earth()!!.mountainWeak())
        val mountainDef = WeakReference<Mountain?>(DI.earth()!!.mountainDefaultFactory())
        val mountainDef2 = WeakReference<Mountain?>(DI.earth()!!.mountainDefault2Factory())
        val riverStrong = WeakReference<River?>(DI.earth()!!.riverStrong())
        val riverSoft = WeakReference<River?>(DI.earth()!!.riverSoft())
        val riverWeak = WeakReference<River?>(DI.earth()!!.riverWeak())
        val riverDef = WeakReference<River?>(DI.earth()!!.riverDefaultSoft())
        val DIPro: GcGodExtComponent = GcGodExtComponentStoneComponent()
        DIPro.extOf(DI)
        val desertStrong = WeakReference<Desert?>(DIPro.earth()!!.desertStrong())
        val desertSoft = WeakReference<Desert?>(DIPro.earth()!!.desertSoft())
        val desertWeak = WeakReference<Desert?>(DIPro.earth()!!.desertWeak())
        val desertFactory = WeakReference(DIPro.earth()!!.desertFactory())
        val waterFlowStrong = WeakReference<WaterFlow?>(DIPro.earth()!!.riverStrong())
        val waterFlowSoft = WeakReference<WaterFlow?>(DIPro.earth()!!.riverSoft())
        val waterFlowWeak = WeakReference<WaterFlow?>(DIPro.earth()!!.riverWeak())
        val waterFlowDefRef = WeakReference<WaterFlow?>(DIPro.earth()!!.riverDefaultSoft())

        //When
        DI.gcStrong()

        //Then
        for (ref in listOf(
            mountainStrong, mountainWeak, mountainDef, mountainDef2,
            riverStrong, riverWeak, riverDef,
            desertStrong, desertWeak, desertFactory,
            waterFlowStrong, waterFlowWeak, waterFlowDefRef
        )) {
            assertNull(ref.get())
        }

        for (ref in listOf(
            mountainSoft,
            riverSoft,
            desertSoft,
            waterFlowSoft
        )) {
            assertNotNull(ref.get())
        }
    }

    @Test
    fun gcSoftTest() {
        //Given
        val DI: GcGodComponent = GcGodComponentStoneComponent()
        val mountainStrong = WeakReference<Mountain?>(DI.earth()?.mountainStrong())
        val mountainSoft = WeakReference<Mountain?>(DI.earth()!!.mountainSoft())
        val mountainWeak = WeakReference<Mountain?>(DI.earth()!!.mountainWeak())
        val mountainDef = WeakReference<Mountain?>(DI.earth()!!.mountainDefaultFactory())
        val mountainDef2 = WeakReference<Mountain?>(DI.earth()!!.mountainDefault2Factory())
        val riverStrong = WeakReference<River?>(DI.earth()!!.riverStrong())
        val riverSoft = WeakReference<River?>(DI.earth()!!.riverSoft())
        val riverWeak = WeakReference<River?>(DI.earth()!!.riverWeak())
        val riverDef = WeakReference<River?>(DI.earth()!!.riverDefaultSoft())
        val DIPro: GcGodExtComponent = GcGodExtComponentStoneComponent()
        DIPro.extOf(DI)
        val desertStrong = WeakReference<Desert?>(DIPro.earth()!!.desertStrong())
        val desertSoft = WeakReference<Desert?>(DIPro.earth()!!.desertSoft())
        val desertWeak = WeakReference<Desert?>(DIPro.earth()!!.desertWeak())
        val desertFactory = WeakReference(DIPro.earth()!!.desertFactory())
        val waterFlowStrong = WeakReference<WaterFlow?>(DIPro.earth()!!.riverStrong())
        val waterFlowSoft = WeakReference<WaterFlow?>(DIPro.earth()!!.riverSoft())
        val waterFlowWeak = WeakReference<WaterFlow?>(DIPro.earth()!!.riverWeak())
        val waterFlowDefRef = WeakReference<WaterFlow?>(DIPro.earth()!!.riverDefaultSoft())

        //When
        DI.gcSoft()

        //Then
        for (ref in listOf(
            mountainSoft, mountainWeak, mountainDef, mountainDef2,
            riverSoft, riverWeak, riverDef,
            desertSoft, desertWeak, desertFactory,
            waterFlowSoft, waterFlowDefRef, waterFlowWeak
        )) {
            assertNull(ref.get())
        }
        for (ref in listOf(
            mountainStrong,
            riverStrong,
            desertStrong,
            waterFlowStrong
        )) {
            assertNotNull(ref.get())
        }
    }

    @Test
    fun gcWeakTest() {
        //Given
        val DI: GcGodComponent = GcGodComponentStoneComponent()
        val mountainStrong = WeakReference<Mountain?>(DI.earth()?.mountainStrong())
        val mountainSoft = WeakReference<Mountain?>(DI.earth()!!.mountainSoft())
        val mountainWeak = WeakReference<Mountain?>(DI.earth()!!.mountainWeak())
        val mountainDef = WeakReference<Mountain?>(DI.earth()!!.mountainDefaultFactory())
        val mountainDef2 = WeakReference<Mountain?>(DI.earth()!!.mountainDefault2Factory())
        val riverStrong = WeakReference<River?>(DI.earth()!!.riverStrong())
        val riverSoft = WeakReference<River?>(DI.earth()!!.riverSoft())
        val riverWeak = WeakReference<River?>(DI.earth()!!.riverWeak())
        val riverDef = WeakReference<River?>(DI.earth()!!.riverDefaultSoft())
        val DIPro: GcGodExtComponent = GcGodExtComponentStoneComponent()
        DIPro.extOf(DI)
        val desertStrong = WeakReference<Desert?>(DIPro.earth()!!.desertStrong())
        val desertSoft = WeakReference<Desert?>(DIPro.earth()!!.desertSoft())
        val desertWeak = WeakReference<Desert?>(DIPro.earth()!!.desertWeak())
        val desertFactory = WeakReference(DIPro.earth()!!.desertFactory())
        val waterFlowStrong = WeakReference<WaterFlow?>(DIPro.earth()!!.riverStrong())
        val waterFlowSoft = WeakReference<WaterFlow?>(DIPro.earth()!!.riverSoft())
        val waterFlowWeak = WeakReference<WaterFlow?>(DIPro.earth()!!.riverWeak())
        val waterFlowDefRef = WeakReference<WaterFlow?>(DIPro.earth()!!.riverDefaultSoft())

        //When
        DI.gcWeak()

        //Then
        for (ref in listOf(
            mountainWeak, mountainDef, mountainDef2,
            riverWeak, riverDef,
            desertWeak, desertFactory,
            waterFlowWeak, waterFlowDefRef
        )) {
            assertNull(ref.get())
        }
        for (ref in listOf(
            mountainStrong, mountainSoft,
            riverStrong, riverSoft,
            desertStrong, desertSoft,
            waterFlowStrong, waterFlowSoft
        )) {
            assertNotNull(ref.get())
        }
    }

    @Test
    fun gcMountainTest() {
        //Given
        val DI: GcGodComponent = GcGodComponentStoneComponent()
        val mountainStrong = WeakReference<Mountain?>(DI.earth()?.mountainStrong())
        val mountainSoft = WeakReference<Mountain?>(DI.earth()!!.mountainSoft())
        val mountainWeak = WeakReference<Mountain?>(DI.earth()!!.mountainWeak())
        val mountainDef = WeakReference<Mountain?>(DI.earth()!!.mountainDefaultFactory())
        val mountainDef2 = WeakReference<Mountain?>(DI.earth()!!.mountainDefault2Factory())
        val riverStrong = WeakReference<River?>(DI.earth()!!.riverStrong())
        val riverSoft = WeakReference<River?>(DI.earth()!!.riverSoft())
        val riverWeak = WeakReference<River?>(DI.earth()!!.riverWeak())
        val riverDef = WeakReference<River?>(DI.earth()!!.riverDefaultSoft())
        val DIPro: GcGodExtComponent = GcGodExtComponentStoneComponent()
        DIPro.extOf(DI)
        val desertStrong = WeakReference<Desert?>(DIPro.earth()!!.desertStrong())
        val desertSoft = WeakReference<Desert?>(DIPro.earth()!!.desertSoft())
        val desertWeak = WeakReference<Desert?>(DIPro.earth()!!.desertWeak())
        val desertFactory = WeakReference(DIPro.earth()!!.desertFactory())
        val waterFlowStrong = WeakReference<WaterFlow?>(DIPro.earth()!!.riverStrong())
        val waterFlowSoft = WeakReference<WaterFlow?>(DIPro.earth()!!.riverSoft())
        val waterFlowWeak = WeakReference<WaterFlow?>(DIPro.earth()!!.riverWeak())
        val waterFlowDefRef = WeakReference<WaterFlow?>(DIPro.earth()!!.riverDefaultSoft())

        //When
        DI.gcMountains()

        //Then
        for (ref in listOf(
            mountainStrong, mountainSoft, mountainWeak, mountainDef, mountainDef2,
            riverWeak, riverDef,
            desertWeak, desertFactory,
            waterFlowWeak, waterFlowDefRef
        )) {
            assertNull(ref.get())
        }
        for (ref in listOf(
            riverStrong, riverSoft,
            desertStrong, desertSoft,
            waterFlowStrong, waterFlowSoft
        )) {
            assertNotNull(ref.get())
        }
    }

    @org.junit.jupiter.api.Test
    fun gcRiverTest() {
        //Given
        val DI: GcGodComponent = GcGodComponentStoneComponent()
        val mountainStrong = WeakReference<Mountain?>(DI.earth()?.mountainStrong())
        val mountainSoft = WeakReference<Mountain?>(DI.earth()!!.mountainSoft())
        val mountainWeak = WeakReference<Mountain?>(DI.earth()!!.mountainWeak())
        val mountainDef = WeakReference<Mountain?>(DI.earth()!!.mountainDefaultFactory())
        val mountainDef2 = WeakReference<Mountain?>(DI.earth()!!.mountainDefault2Factory())
        val riverStrong = WeakReference<River?>(DI.earth()!!.riverStrong())
        val riverSoft = WeakReference<River?>(DI.earth()!!.riverSoft())
        val riverWeak = WeakReference<River?>(DI.earth()!!.riverWeak())
        val riverDef = WeakReference<River?>(DI.earth()!!.riverDefaultSoft())
        val DIPro: GcGodExtComponent = GcGodExtComponentStoneComponent()
        DIPro.extOf(DI)
        val desertStrong = WeakReference<Desert?>(DIPro.earth()!!.desertStrong())
        val desertSoft = WeakReference<Desert?>(DIPro.earth()!!.desertSoft())
        val desertWeak = WeakReference<Desert?>(DIPro.earth()!!.desertWeak())
        val desertFactory = WeakReference(DIPro.earth()!!.desertFactory())
        val waterFlowStrong = WeakReference<WaterFlow?>(DIPro.earth()!!.riverStrong())
        val waterFlowSoft = WeakReference<WaterFlow?>(DIPro.earth()!!.riverSoft())
        val waterFlowWeak = WeakReference<WaterFlow?>(DIPro.earth()!!.riverWeak())
        val waterFlowDefRef = WeakReference<WaterFlow?>(DIPro.earth()!!.riverDefaultSoft())

        //When
        DI.gcRivers()

        //Then
        for (ref in listOf(
            mountainWeak, mountainDef, mountainDef2,
            riverStrong, riverSoft, riverWeak, riverDef,
            desertWeak, desertFactory,
            waterFlowStrong, waterFlowSoft, waterFlowDefRef, waterFlowWeak
        )) {
            assertNull(ref.get())
        }
        for (ref in listOf(
            mountainStrong, mountainSoft,
            desertStrong, desertSoft
        )) {
            assertNotNull(ref.get())
        }
    }

    @Test
    fun gcMountainAndRiverTest() {
        //Given
        val DI: GcGodComponent = GcGodComponentStoneComponent()
        val mountainStrong = WeakReference<Mountain?>(DI.earth()?.mountainStrong())
        val mountainSoft = WeakReference<Mountain?>(DI.earth()!!.mountainSoft())
        val mountainWeak = WeakReference<Mountain?>(DI.earth()!!.mountainWeak())
        val mountainDef = WeakReference<Mountain?>(DI.earth()!!.mountainDefaultFactory())
        val mountainDef2 = WeakReference<Mountain?>(DI.earth()!!.mountainDefault2Factory())
        val riverStrong = WeakReference<River?>(DI.earth()!!.riverStrong())
        val riverSoft = WeakReference<River?>(DI.earth()!!.riverSoft())
        val riverWeak = WeakReference<River?>(DI.earth()!!.riverWeak())
        val riverDef = WeakReference<River?>(DI.earth()!!.riverDefaultSoft())
        val DIPro: GcGodExtComponent = GcGodExtComponentStoneComponent()
        DIPro.extOf(DI)
        val desertStrong = WeakReference<Desert?>(DIPro.earth()!!.desertStrong())
        val desertSoft = WeakReference<Desert?>(DIPro.earth()!!.desertSoft())
        val desertWeak = WeakReference<Desert?>(DIPro.earth()!!.desertWeak())
        val desertFactory = WeakReference(DIPro.earth()!!.desertFactory())
        val waterFlowStrong = WeakReference<WaterFlow?>(DIPro.earth()!!.riverStrong())
        val waterFlowSoft = WeakReference<WaterFlow?>(DIPro.earth()!!.riverSoft())
        val waterFlowWeak = WeakReference<WaterFlow?>(DIPro.earth()!!.riverWeak())
        val waterFlowDefRef = WeakReference<WaterFlow?>(DIPro.earth()!!.riverDefaultSoft())

        //When
        DI.gcMountainsAndRivers()

        //Then
        for (ref in listOf(
            mountainStrong, mountainSoft, mountainWeak, mountainDef, mountainDef2,
            riverStrong, riverSoft, riverWeak, riverDef,
            desertFactory, desertWeak,
            waterFlowStrong, waterFlowSoft, waterFlowDefRef, waterFlowWeak
        )) {
            assertNull(ref.get())
        }

        for (ref in listOf(
            desertStrong, desertSoft
        )) {
            assertNotNull(ref.get())
        }
    }


    @Test
    fun gcSoftMountainTest() {
        //Given
        val DI: GcGodComponent = GcGodComponentStoneComponent()
        val mountainStrong = WeakReference<Mountain?>(DI.earth()?.mountainStrong())
        val mountainSoft = WeakReference<Mountain?>(DI.earth()!!.mountainSoft())
        val mountainWeak = WeakReference<Mountain?>(DI.earth()!!.mountainWeak())
        val mountainDef = WeakReference<Mountain?>(DI.earth()!!.mountainDefaultFactory())
        val mountainDef2 = WeakReference<Mountain?>(DI.earth()!!.mountainDefault2Factory())
        val riverStrong = WeakReference<River?>(DI.earth()!!.riverStrong())
        val riverSoft = WeakReference<River?>(DI.earth()!!.riverSoft())
        val riverWeak = WeakReference<River?>(DI.earth()!!.riverWeak())
        val riverDef = WeakReference<River?>(DI.earth()!!.riverDefaultSoft())
        val DIPro: GcGodExtComponent = GcGodExtComponentStoneComponent()
        DIPro.extOf(DI)
        val desertStrong = WeakReference<Desert?>(DIPro.earth()!!.desertStrong())
        val desertSoft = WeakReference<Desert?>(DIPro.earth()!!.desertSoft())
        val desertWeak = WeakReference<Desert?>(DIPro.earth()!!.desertWeak())
        val desertFactory = WeakReference(DIPro.earth()!!.desertFactory())
        val waterFlowStrong = WeakReference<WaterFlow?>(DIPro.earth()!!.riverStrong())
        val waterFlowSoft = WeakReference<WaterFlow?>(DIPro.earth()!!.riverSoft())
        val waterFlowWeak = WeakReference<WaterFlow?>(DIPro.earth()!!.riverWeak())
        val waterFlowDefRef = WeakReference<WaterFlow?>(DIPro.earth()!!.riverDefaultSoft())

        //When
        DI.gcSoftMountains()

        //Then
        for (ref in listOf(
            mountainSoft, mountainWeak, mountainDef, mountainDef2,
            riverWeak, riverDef,
            desertFactory, desertWeak,
            waterFlowWeak, waterFlowDefRef
        )) {
            assertNull(ref.get())
        }
        for (ref in listOf(
            mountainStrong,
            riverStrong, riverSoft,
            desertStrong, desertSoft,
            waterFlowStrong, waterFlowSoft
        )) {
            assertNotNull(ref.get())
        }
    }

    @Test
    fun gcStrongMountainTest() {
        //Given
        val DI: GcGodComponent = GcGodComponentStoneComponent()
        val mountainStrong = WeakReference<Mountain?>(DI.earth()?.mountainStrong())
        val mountainSoft = WeakReference<Mountain?>(DI.earth()!!.mountainSoft())
        val mountainWeak = WeakReference<Mountain?>(DI.earth()!!.mountainWeak())
        val mountainDef = WeakReference<Mountain?>(DI.earth()!!.mountainDefaultFactory())
        val mountainDef2 = WeakReference<Mountain?>(DI.earth()!!.mountainDefault2Factory())
        val riverStrong = WeakReference<River?>(DI.earth()!!.riverStrong())
        val riverSoft = WeakReference<River?>(DI.earth()!!.riverSoft())
        val riverWeak = WeakReference<River?>(DI.earth()!!.riverWeak())
        val riverDef = WeakReference<River?>(DI.earth()!!.riverDefaultSoft())
        val DIPro: GcGodExtComponent = GcGodExtComponentStoneComponent()
        DIPro.extOf(DI)
        val desertStrong = WeakReference<Desert?>(DIPro.earth()!!.desertStrong())
        val desertSoft = WeakReference<Desert?>(DIPro.earth()!!.desertSoft())
        val desertWeak = WeakReference<Desert?>(DIPro.earth()!!.desertWeak())
        val desertFactory = WeakReference(DIPro.earth()!!.desertFactory())
        val waterFlowStrong = WeakReference<WaterFlow?>(DIPro.earth()!!.riverStrong())
        val waterFlowSoft = WeakReference<WaterFlow?>(DIPro.earth()!!.riverSoft())
        val waterFlowWeak = WeakReference<WaterFlow?>(DIPro.earth()!!.riverWeak())
        val waterFlowDefRef = WeakReference<WaterFlow?>(DIPro.earth()!!.riverDefaultSoft())

        //When
        DI.gcStrongMountains()

        //Then
        for (ref in listOf(
            mountainStrong, mountainWeak, mountainDef, mountainDef2,
            riverWeak, riverDef,
            desertFactory, desertWeak,
            waterFlowWeak, waterFlowDefRef
        )) {
            assertNull(ref.get())
        }
        for (ref in listOf(
            mountainSoft,
            riverStrong, riverSoft,
            desertStrong, desertSoft,
            waterFlowStrong, waterFlowSoft
        )) {
            assertNotNull(ref.get())
        }
    }

}
