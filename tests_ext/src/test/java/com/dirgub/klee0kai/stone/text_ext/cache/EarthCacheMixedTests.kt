package com.dirgub.klee0kai.stone.text_ext.cache

import com.github.klee0kai.test.di.gcforest.GcGodComponent
import com.github.klee0kai.test.di.gcforest.GcGodComponentStoneComponent
import com.github.klee0kai.test.mowgli.earth.Mountain
import com.github.klee0kai.test_ext.inject.di.gcforest.GcGodExtComponent
import com.github.klee0kai.test_ext.inject.di.gcforest.GcGodExtComponentStoneComponent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class EarthCacheMixedTests {

    @Test
    fun strongCacheTest() {
        //Given
        val DI: GcGodComponent = GcGodComponentStoneComponent()
        val DIPro: GcGodExtComponent = GcGodExtComponentStoneComponent()
        DIPro.extOf(DI)

        //When
        val mountain1: Mountain? = DI.earth()!!.mountainStrong()
        val mountain2: Mountain? = DIPro.earth()!!.mountainStrong()

        //Then
        assertEquals(
            mountain1!!.uuid,
            mountain2!!.uuid
        )
    }


    @Test
    fun softCacheTest() {
        //Given
        val DI: GcGodComponent = GcGodComponentStoneComponent()
        val DIPro: GcGodExtComponent = GcGodExtComponentStoneComponent()
        DIPro.extOf(DI)

        //When
        val mountain1: Mountain? = DI.earth()!!.mountainSoft()
        val mountain2: Mountain? = DIPro.earth()!!.mountainSoft()

        //Then
        assertEquals(
            mountain1!!.uuid,
            mountain2!!.uuid
        )
    }

    @Test
    fun weakCacheTest() {
        //Given
        val DI: GcGodComponent = GcGodComponentStoneComponent()
        val DIPro: GcGodExtComponent = GcGodExtComponentStoneComponent()
        DIPro.extOf(DI)

        //When
        val mountain1: Mountain? = DI.earth()!!.mountainWeak()
        val mountain2: Mountain? = DIPro.earth()!!.mountainWeak()

        //Then
        assertEquals(
            mountain1!!.uuid,
            mountain2!!.uuid
        )
    }

    @Test
    fun defCacheTest() {
        //Given
        val DI: GcGodComponent = GcGodComponentStoneComponent()
        val DIPro: GcGodExtComponent = GcGodExtComponentStoneComponent()
        DIPro.extOf(DI)

        //When
        val mountain1: Mountain? = DI.earth()!!.mountainDefaultFactory()
        val mountain2: Mountain? = DIPro.earth()!!.mountainDefaultFactory()

        //Then
        assertNotEquals(
            mountain1!!.uuid,
            mountain2!!.uuid,
            "Factory providing"
        )
    }

    @Test
    fun defCache2Test() {
        //Given
        val DI: GcGodComponent = GcGodComponentStoneComponent()
        val DIPro: GcGodExtComponent = GcGodExtComponentStoneComponent()
        DIPro.extOf(DI)

        //When
        val mountain1: Mountain? = DI.earth()!!.mountainDefault2Factory()
        val mountain2: Mountain? = DIPro.earth()!!.mountainDefault2Factory()

        //Then
        assertNotEquals(
            mountain1!!.uuid,
            mountain2!!.uuid,
            "Factory providing"
        )
    }

    @Test
    fun differentMethodDifferentCacheTest() {
        //Given
        val DI: GcGodComponent = GcGodComponentStoneComponent()
        val DIPro: GcGodExtComponent = GcGodExtComponentStoneComponent()
        DIPro.extOf(DI)

        //When
        val mountainStrong: Mountain? = DI.earth()!!.mountainStrong()
        val mountainSoft: Mountain? = DIPro.earth()!!.mountainSoft()

        //Then
        assertNotEquals(
            mountainStrong!!.uuid,
            mountainSoft!!.uuid
        )
    }


    @Test
    fun factoryNotCacheTest() {
        //Given
        val DI: GcGodComponent = GcGodComponentStoneComponent()
        val DIPro: GcGodExtComponent = GcGodExtComponentStoneComponent()
        DIPro.extOf(DI)

        //When
        val mountain1: Mountain? = DI.earth()!!.mountainFactory()
        val mountain2: Mountain? = DIPro.earth()!!.mountainFactory()

        //Then
        assertNotEquals(
            mountain1!!.uuid,
            mountain2!!.uuid
        )
    }


    @Test
    fun differentDIDifferentCacheTest() {
        //Given
        val DI1: GcGodComponent = GcGodComponentStoneComponent()
        val DIPro1: GcGodExtComponent = GcGodExtComponentStoneComponent()
        DIPro1.extOf(DI1)
        val DI2: GcGodComponent = GcGodComponentStoneComponent()
        val DIPro2: GcGodExtComponent = GcGodExtComponentStoneComponent()
        DIPro2.extOf(DI2)

        //When
        val mountain1: Mountain? = DI1.earth()!!.mountainStrong()
        val mountain2: Mountain? = DIPro2.earth()!!.mountainStrong()

        //Then
        assertNotEquals(
            mountain1!!.uuid,
            mountain2!!.uuid
        )
    }
}
