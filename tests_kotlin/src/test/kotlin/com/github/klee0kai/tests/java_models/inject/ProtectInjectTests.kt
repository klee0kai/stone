package com.github.klee0kai.tests.java_models.inject

import com.github.klee0kai.test.mowgli.Forest
import com.github.klee0kai.test.mowgli.animal.Horse
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.lang.ref.WeakReference

class ProtectInjectTests {
    @Test
    fun withoutProtectInjectTest() {
        // Given
        val forest = Forest()
        forest.create()

        //When
        var horse: Horse? = Horse()
        horse!!.born()
        val historyWeakReference = WeakReference(
            horse.history
        )
        horse = null
        Forest.DI.gcAll()

        //Then: without protect all not uses should be garbage collected
        assertNull(historyWeakReference.get())
    }

    @Test
    @Throws(InterruptedException::class)
    fun withProtectInjectTest() {
        // Given
        val forest = Forest()
        forest.create()

        //When
        var horse: Horse? = Horse()
        horse!!.born()
        val historyWeakReference = WeakReference(
            horse.history
        )
        Forest.DI.protectInjected(horse)
        horse = null
        Forest.DI.gcAll()

        //Then
        Assertions.assertNotNull(historyWeakReference.get())

        //after protect finished
        Thread.sleep(60)
        Forest.DI.gcAll()
        assertNull(historyWeakReference.get())
    }
}