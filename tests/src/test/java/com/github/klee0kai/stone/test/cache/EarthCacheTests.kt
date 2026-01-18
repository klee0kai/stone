package com.github.klee0kai.stone.test.cache

import com.github.klee0kai.test.di.gcforest.GcGodComponentStoneComponent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class EarthCacheTests {

    @Test
    fun strongCacheTest() {
        //Given
        val DI = GcGodComponentStoneComponent()

        //When
        val mountain1 = DI.earth().mountainStrong()
        val mountain2 = DI.earth().mountainStrong()

        //Then
        assertEquals(
            mountain1?.uuid,
            mountain2?.uuid
        )
    }


    @Test
    fun softCacheTest() {
        //Given
        val DI = GcGodComponentStoneComponent()

        //When
        val mountain1 = DI.earth().mountainSoft()
        val mountain2 = DI.earth().mountainSoft()

        //Then
        assertEquals(
            mountain1?.uuid,
            mountain2?.uuid
        )
    }

    @Test
    fun weakCacheTest() {
        //Given
        val DI = GcGodComponentStoneComponent()

        //When
        val mountain1 = DI.earth().mountainWeak()
        val mountain2 = DI.earth().mountainWeak()

        //Then
        assertEquals(
            mountain1?.uuid,
            mountain2?.uuid
        )
    }

    @Test
    fun defCacheTest() {
        //Given
        val DI = GcGodComponentStoneComponent()

        //When
        val mountain1 = DI.earth().mountainDefaultFactory()
        val mountain2 = DI.earth().mountainDefaultFactory()

        //Then
        assertNotEquals(
            mountain1?.uuid,
            mountain2?.uuid,
            "Default is factory providing"
        )
    }

    @Test
    fun defCache2Test() {
        //Given
        val DI = GcGodComponentStoneComponent()

        //When
        val mountain1 = DI.earth().mountainDefault2Factory()
        val mountain2 = DI.earth().mountainDefault2Factory()

        //Then
        assertNotEquals(
            mountain1?.uuid,
            mountain2?.uuid,
            "Default is factory providing"
        )
    }

    @Test
    fun differentMethodDifferentCacheTest() {
        //Given
        val DI = GcGodComponentStoneComponent()

        //When
        val mountainStrong = DI.earth().mountainStrong()
        val mountainSoft = DI.earth().mountainSoft()

        //Then
        assertNotEquals(
            mountainStrong?.uuid,
            mountainSoft?.uuid
        )
    }


    @Test
    fun factoryNotCacheTest() {
        //Given
        val DI = GcGodComponentStoneComponent()

        //When
        val mountain1 = DI.earth().mountainFactory()
        val mountain2 = DI.earth().mountainFactory()

        //Then
        assertNotEquals(
            mountain1?.uuid,
            mountain2?.uuid
        )
    }


    @Test
    fun differentDIDifferentCacheTest() {
        //Given
        val DI1 = GcGodComponentStoneComponent()
        val DI2 = GcGodComponentStoneComponent()

        //When
        val mountain1 = DI1.earth().mountainStrong()
        val mountain2 = DI2.earth().mountainStrong()

        //Then
        assertNotEquals(
            mountain1?.uuid,
            mountain2?.uuid,
        )
    }

}
