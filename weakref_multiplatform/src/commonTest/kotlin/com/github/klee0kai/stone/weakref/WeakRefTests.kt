package com.github.klee0kai.stone.weakref

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class WeakRefTests {

    @Test
    fun clearRefTest() {
        val ref = WeakRef("some text")

        assertEquals("some text", ref.get())

        ref.clear()

        assertNull(ref.get())
    }

}