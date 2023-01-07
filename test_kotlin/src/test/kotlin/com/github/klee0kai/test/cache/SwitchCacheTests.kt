package com.github.klee0kai.test.cache

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.cache.di.CacheTestComponent
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.lang.ref.WeakReference

class SwitchCacheTests {
    @Test
    fun strongToWeakTest() {
        val DI = Stone.createComponent(CacheTestComponent::class.java)
        val repStrongRef = WeakReference(DI.data().provideStrong())

        System.gc()
        assertNotNull(repStrongRef.get())
        DI.allWeak()


        System.gc()
        assertNull(repStrongRef.get())
    }

    @Test
    fun strongToWeak2Test() {
        val DI = Stone.createComponent(CacheTestComponent::class.java)
        val repStrongRef = WeakReference(DI.data().provideStrong())
        val repSoftRef = WeakReference(DI.data().provideSoft())

        System.gc()
        assertNotNull(repStrongRef.get())
        assertNotNull(repSoftRef.get())


        DI.strongToWeak()
        System.gc()
        assertNull(repStrongRef.get())
        assertNotNull(repSoftRef.get())


        DI.allWeak()
        System.gc()
        assertNull(repSoftRef.get())
    }

    @Test
    @Throws(InterruptedException::class)
    fun weakToStrongFewMillisTest() {
        val DI = Stone.createComponent(CacheTestComponent::class.java)
        val repWeakRef = WeakReference(DI.data().provideWeak())

        assertNotNull(repWeakRef.get())
        DI.allStrongFewMillis()

        System.gc()
        System.gc()
        assertNotNull(repWeakRef.get())

        Thread.sleep(110)
        System.gc()
        assertNull(repWeakRef.get())
    }
}