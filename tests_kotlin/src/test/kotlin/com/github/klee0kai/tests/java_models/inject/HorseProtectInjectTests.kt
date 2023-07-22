package com.github.klee0kai.tests.java_models.inject

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.stone.lifecycle.StoneLifeCycleListener
import com.github.klee0kai.test.di.base_forest.ForestComponent
import com.github.klee0kai.test.mowgli.animal.Horse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.lang.ref.WeakReference

class HorseProtectInjectTests {
    @Test
    fun withoutProtectInjectTest() {
        // Given
        val DI = Stone.createComponent(ForestComponent::class.java)
        var horse: Horse? = Horse()


        //When
        DI.inject(horse) { listener: StoneLifeCycleListener? -> }
        val historyWeakReference = WeakReference(
            horse!!.history
        )
        horse = null
        DI.gcAll()

        //Then: without protect all not uses should be garbage collected
        assertNull(historyWeakReference.get())
    }

    @Test
    @Throws(InterruptedException::class)
    fun withProtectInjectTest() {
        // Given
        val DI = Stone.createComponent(ForestComponent::class.java)
        var horse: Horse? = Horse()

        //When
        DI.inject(horse) { listener: StoneLifeCycleListener? -> }
        val historyWeakReference = WeakReference(
            horse!!.history
        )
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