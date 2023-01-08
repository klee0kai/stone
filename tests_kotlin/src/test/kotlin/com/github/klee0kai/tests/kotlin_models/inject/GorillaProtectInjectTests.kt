package com.github.klee0kai.tests.kotlin_models.inject

import com.github.klee0kai.test_kotlin.mowgli.RainForest
import com.github.klee0kai.test_kotlin.mowgli.animal.Gorilla
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.lang.ref.WeakReference

class GorillaProtectInjectTests {

    @Test
    fun withoutProtectInjectTest() {
        // Given
        val forest = RainForest()
        forest.create()

        //When
        var gorilla: Gorilla? = Gorilla()
        gorilla!!.born()
        val historyWeakReference = WeakReference(gorilla.history)
        gorilla = null
        RainForest.DI.gcAll()

        //Then: without protect all not uses should be garbage collected
        assertNull(historyWeakReference.get())
    }


    @Test
    @Throws(InterruptedException::class)
    fun withProtectInjectTest() {
        // Given
        val forest = RainForest()
        forest.create()


        //When
        var gorrila: Gorilla? = Gorilla()
        gorrila!!.born()
        val historyWeakReference = WeakReference(gorrila.history)
        RainForest.DI.protectInjected(gorrila)
        gorrila = null
        RainForest.DI.gcAll()

        //Then
        assertNotNull(historyWeakReference.get())

        //after protect finished
        Thread.sleep(60)
        RainForest.DI.gcAll()
        assertNull(historyWeakReference.get())
    }


}