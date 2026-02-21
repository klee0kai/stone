package com.github.klee0kai.stone.test.inject

import com.github.klee0kai.stone.weakref.WeakRef
import com.github.klee0kai.test.di.base_forest.ForestComponentStoneComponent
import com.github.klee0kai.test.mowgli.animal.Horse
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class HorseProtectInjectGcTests {


    @Test
    fun withProtectInjectTest() {
        // Given
        val DI = ForestComponentStoneComponent()
        var horse: Horse? = Horse()

        //When
        DI.inject(
            horse,
            stoneLifeCycleOwner = {}
        )

        val historyWeakReference = WeakRef(horse!!.history)
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
