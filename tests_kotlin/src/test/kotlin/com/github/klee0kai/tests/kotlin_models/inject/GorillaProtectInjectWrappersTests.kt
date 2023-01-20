package com.github.klee0kai.tests.kotlin_models.inject

import com.github.klee0kai.test_kotlin.mowgli.RainForest
import com.github.klee0kai.test_kotlin.mowgli.University
import com.github.klee0kai.test_kotlin.mowgli.animal.Gorilla
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.lang.ref.WeakReference

class GorillaProtectInjectWrappersTests {

    @Test
    fun lazyWrapperProtectTest() {
        // Given
        val forest = RainForest()
        forest.create()

        //When
        val university = University()
        university.build()
        val history = WeakReference(university.historyLazyProvide.get())
        RainForest.DI.protectInjected(university)
        RainForest.DI.gcAll()

        //Then
        assertNotNull(history.get())
    }


    @Test
    @Throws(InterruptedException::class)
    fun withProtectInjectTest() {
        // Given
        val forest = RainForest()
        forest.create()

        //When
        var gorilla: Gorilla? = Gorilla()
        gorilla!!.born()
        val historyWeakReference = WeakReference(gorilla.history)
        RainForest.DI.protectInjected(gorilla)
        gorilla = null
        RainForest.DI.gcAll()

        //Then
        assertNotNull(historyWeakReference.get())

        //after protect finished
        Thread.sleep(50)
        System.gc()
        assertNull(historyWeakReference.get())
    }

}