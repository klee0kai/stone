package com.github.klee0kai.stone.weakref

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class WeakRefTests {

    class TestClass(
        val string: String,
    )

    @Test
    fun clearRefTest() {
        val ref = WeakRef(TestClass("some text"))

        assertEquals("some text", ref.get()?.string)

        ref.clear()

        assertNull(ref.get())
    }

}