package com.github.klee0kai.tests.java_models.cache

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.di.gcforest.GcGodComponent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class EarthCacheTests {
    @Test
    fun strongCacheTest() {
        //Given
        val di = Stone.createComponent(GcGodComponent::class.java)

        //When
        val mountain1 = di.earth().mountainStrong()
        val mountain2 = di.earth().mountainStrong()

        //Then
        assertEquals(
            mountain1.uuid,
            mountain2.uuid
        )
    }

    @Test
    fun softCacheTest() {
        //Given
        val di = Stone.createComponent(GcGodComponent::class.java)

        //When
        val mountain1 = di.earth().mountainSoft()
        val mountain2 = di.earth().mountainSoft()

        //Then
        assertEquals(
            mountain1.uuid,
            mountain2.uuid
        )
    }

    @Test
    fun weakCacheTest() {
        //Given
        val di = Stone.createComponent(GcGodComponent::class.java)

        //When
        val mountain1 = di.earth().mountainWeak()
        val mountain2 = di.earth().mountainWeak()

        //Then
        assertEquals(
            mountain1.uuid,
            mountain2.uuid
        )
    }

    @Test
    fun defCacheTest() {
        //Given
        val di = Stone.createComponent(GcGodComponent::class.java)

        //When
        val mountain1 = di.earth().mountainDefaultSoft()
        val mountain2 = di.earth().mountainDefaultSoft()

        //Then
        assertEquals(
            mountain1.uuid,
            mountain2.uuid
        )
    }

    @Test
    fun differentMethodDifferentCacheTest() {
        //Given
        val di = Stone.createComponent(GcGodComponent::class.java)

        //When
        val mountainStrong = di.earth().mountainStrong()
        val mountainSoft = di.earth().mountainSoft()

        //Then
        assertNotEquals(
            mountainStrong.uuid,
            mountainSoft.uuid
        )
    }

    @Test
    fun factoryNotCacheTest() {
        //Given
        val di = Stone.createComponent(GcGodComponent::class.java)

        //When
        val mountain1 = di.earth().mountainFactory()
        val mountain2 = di.earth().mountainFactory()

        //Then
        assertNotEquals(
            mountain1.uuid,
            mountain2.uuid
        )
    }

    @Test
    fun differentDIDifferentCacheTest() {
        //Given
        val di1 = Stone.createComponent(GcGodComponent::class.java)
        val di2 = Stone.createComponent(GcGodComponent::class.java)

        //When
        val mountain1 = di1.earth().mountainStrong()
        val mountain2 = di2.earth().mountainStrong()

        //Then
        assertNotEquals(
            mountain1.uuid,
            mountain2.uuid
        )
    }
}