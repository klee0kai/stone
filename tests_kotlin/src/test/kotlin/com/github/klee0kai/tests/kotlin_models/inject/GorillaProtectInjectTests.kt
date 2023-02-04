package com.github.klee0kai.tests.kotlin_models.inject

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test_kotlin.di.base_forest.RainForestComponent
import com.github.klee0kai.test_kotlin.mowgli.animal.Gorilla
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.lang.ref.WeakReference

class GorillaProtectInjectTests {

    @Test
    fun withoutProtectInjectTest() {
        // Given
        val DI = Stone.createComponent(RainForestComponent::class.java)
        var gorilla: Gorilla? = Gorilla()


        //When
        DI.inject(gorilla!!)
        val historyWeakReference = WeakReference(gorilla.history)
        gorilla = null
        DI.gcAll()

        //Then: without protect all not uses should be garbage collected
        assertNull(historyWeakReference.get())
    }


    @Test
    @Throws(InterruptedException::class)
    fun withProtectInjectTest() {
        // Given
        val DI = Stone.createComponent(RainForestComponent::class.java)
        var gorrila: Gorilla? = Gorilla()


        //When
        DI.inject(gorrila!!)
        val historyWeakReference = WeakReference(gorrila.history)
        DI.protectInjected(gorrila)
        gorrila = null
        DI.gcAll()

        //Then
        assertNotNull(historyWeakReference.get())

        //after protect finished
        Thread.sleep(100)
        DI.gcAll()
        assertNull(historyWeakReference.get())
    }


}