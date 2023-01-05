package com.github.klee0kai.test.cache

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.cache.di.CacheTestComponent
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class CacheTests {
    @Test
    fun simpleCacheTest() {
        val DI = Stone.createComponent(CacheTestComponent::class.java)

        val repFactory1 = DI.data().provideFactory()
        val repFactory2 = DI.data().provideFactory()
        val repFactory3 = DI.data().provideFactory()

        assertNotEquals(repFactory1.uuid, repFactory2.uuid)
        assertNotEquals(repFactory2.uuid, repFactory3.uuid)


        val repStrong1 = DI.data().provideStrong()
        val repStrong2 = DI.data().provideStrong()
        val repStrong3 = DI.data().provideStrong()
        assertNotEquals(repFactory1.uuid, repStrong1.uuid)
        assertEquals(repStrong1.uuid, repStrong2.uuid)
        assertEquals(repStrong2.uuid, repStrong3.uuid)


        val repSoft1 = DI.data().provideSoft()
        val repSoft2 = DI.data().provideSoft()
        val repSoft3 = DI.data().provideSoft()
        assertNotEquals(repFactory1.uuid, repStrong1.uuid)
        assertNotEquals(repStrong1.uuid, repSoft1.uuid)
        assertEquals(repSoft1.uuid, repSoft2.uuid)
        assertEquals(repSoft2.uuid, repSoft3.uuid)


        val repWeak1 = DI.data().provideWeak()
        val repWeak2 = DI.data().provideWeak()
        val repWeak3 = DI.data().provideWeak()
        assertNotEquals(repWeak1.uuid, repStrong1.uuid)
        assertNotEquals(repWeak2.uuid, repSoft1.uuid)
        assertEquals(repWeak1.uuid, repWeak2.uuid)
        assertEquals(repWeak2.uuid, repWeak3.uuid)


        val repDefSoft1 = DI.data().provideDefaultSoft()
        val repDefSoft2 = DI.data().provideDefaultSoft()
        val repDefSoft3 = DI.data().provideDefaultSoft()
        assertNotEquals(repDefSoft1.uuid, repStrong1.uuid)
        assertNotEquals(repDefSoft1.uuid, repSoft1.uuid)
        assertEquals(repDefSoft1.uuid, repDefSoft2.uuid)
        assertEquals(repDefSoft2.uuid, repDefSoft3.uuid)
    }

    @Test
    fun refLifecycleTest() {
        val DI = Stone.createComponent(CacheTestComponent::class.java)
    }

    @Test
    fun separatedDITTest() {
        val DI1 = Stone.createComponent(CacheTestComponent::class.java)
        val DI2 = Stone.createComponent(CacheTestComponent::class.java)
    }
}