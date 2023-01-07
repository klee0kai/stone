package com.github.klee0kai.test.gc

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.gc.di.GComponent
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.lang.ref.WeakReference

class GcScopesTests {
    @Test
    fun allScopeTest() {
        val DI = Stone.createComponent(GComponent::class.java)
        val weakStone = WeakReference(DI.data().provideWeak())
        val softStone = WeakReference(DI.data().provideSoft())
        val strongStone = WeakReference(DI.data().provideStrong())
        val soft2Stone = WeakReference(DI.data().provideDefaultSoft())

        DI.gcAll()
        assertNull(weakStone.get())
        assertNull(softStone.get())
        assertNull(strongStone.get())
        assertNull(soft2Stone.get())
    }

    @Test
    fun weakScopeTest() {
        val DI = Stone.createComponent(GComponent::class.java)
        val weakStone = WeakReference(DI.data().provideWeak())
        val softStone = WeakReference(DI.data().provideSoft())
        val strongStone = WeakReference(DI.data().provideStrong())
        val soft2Stone = WeakReference(DI.data().provideDefaultSoft())


        var i = 0
        DI.gcWeak()
        assertNull(weakStone.get())
        assertNotNull(softStone.get())
        assertNotNull(strongStone.get())
        assertNotNull(soft2Stone.get())
    }

    @Test
    fun softScopeTest() {
        val DI = Stone.createComponent(GComponent::class.java)
        val weakStone = WeakReference(DI.data().provideWeak())
        val softStone = WeakReference(DI.data().provideSoft())
        val strongStone = WeakReference(DI.data().provideStrong())
        val soft2Stone = WeakReference(DI.data().provideDefaultSoft())

        DI.gcSoft()
        assertNull(weakStone.get())
        assertNull(softStone.get())
        assertNotNull(strongStone.get())
        assertNull(soft2Stone.get())
    }

    @Test
    fun strongScopeTest() {
        val DI = Stone.createComponent(GComponent::class.java)
        val weakStone = WeakReference(DI.data().provideWeak())
        val softStone = WeakReference(DI.data().provideSoft())
        val strongStone = WeakReference(DI.data().provideStrong())
        val soft2Stone = WeakReference(DI.data().provideDefaultSoft())


        DI.gcStrong()
        assertNull(weakStone.get())
        assertNotNull(softStone.get())
        assertNull(strongStone.get())
        assertNotNull(soft2Stone.get())
    }
}