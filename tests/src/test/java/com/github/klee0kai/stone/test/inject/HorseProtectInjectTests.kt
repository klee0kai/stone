package com.github.klee0kai.stone.test.inject

import com.github.klee0kai.test.di.base_forest.ForestComponentStoneComponent
import com.github.klee0kai.test.mowgli.animal.Horse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import java.lang.ref.WeakReference

class HorseProtectInjectTests {

    @Test
    fun withoutProtectInjectTest() {
        // Given
        val DI = ForestComponentStoneComponent()
        var horse: Horse? = Horse()


        //When
        DI.inject(
            horse,
            stoneLifeCycleOwner = { }
        )

        val historyWeakReference = WeakReference(horse?.history)
        horse = null
        System.gc()

        //Then: without protect all not uses should be garbage collected
        assertNull(historyWeakReference.get())
    }

    @Test
    @Throws(InterruptedException::class)
    fun withProtectInjectTest() {
        // Given
        val DI = ForestComponentStoneComponent()
        var horse: Horse? = Horse()

        //When
        DI.inject(
            horse,
            stoneLifeCycleOwner = {}
        )

        val historyWeakReference = WeakReference(horse!!.history)
        DI.protectInjected(horse)
        horse = null
        DI.gcAll()

        //Then
        assertNotNull(historyWeakReference.get())

        //after protect finished
        Thread.sleep(50)
        DI.gcAll()
        assertNull(historyWeakReference.get())
    }

}
