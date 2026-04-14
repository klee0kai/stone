package com.github.klee0kai.stone.weakref

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertNotEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SoftRefTests {

    class TestClass(
        val string: String,
    )

    @Test
    fun clearRefTest() {
        val ref = SoftRef(TestClass("some text"))

        assertEquals("some text", ref.get()?.string)

        ref.clear()

        assertNull(ref.get())
    }

    // ==================== equals tests ====================

    @Test
    fun equalsReflexivity() {
        val ref = SoftRef(TestClass("test"))
        assertTrue(ref.equals(ref), "Reference should equal itself")
    }

    @Test
    fun equalsWithNull() {
        val ref = SoftRef(TestClass("test"))
        assertFalse(ref.equals(null), "Reference should not equal null")
    }

    @Test
    fun equalsWithDifferentType() {
        val ref = SoftRef(TestClass("test"))
        val obj = TestClass("test")
        assertFalse(ref.equals(obj), "SoftRef should not equal non-SoftRef object")
    }

    @Test
    fun equalsWithSameContent() {
        val testObj = TestClass("test")
        val ref1 = SoftRef(testObj)
        val ref2 = SoftRef(testObj)
        assertTrue(ref1.equals(ref2), "SoftRefs with same referent should be equal")
    }

    @Test
    fun equalsWithDifferentContent() {
        val ref1 = SoftRef(TestClass("test1"))
        val ref2 = SoftRef(TestClass("test2"))
        assertFalse(ref1.equals(ref2), "SoftRefs with different referents should not be equal")
    }

    @Test
    fun equalsAfterClear() {
        val testObj = TestClass("test")
        val ref1 = SoftRef(testObj)
        val ref2 = SoftRef(testObj)

        assertTrue(ref1.equals(ref2), "SoftRefs should be equal before clearing")

        ref1.clear()

        assertFalse(ref1.equals(ref2), "SoftRef cleared should not equal non-cleared with same referent")
    }

    @Test
    fun equalsWithBothCleared() {
        val ref1 = SoftRef(TestClass("test"))
        val ref2 = SoftRef(TestClass("test"))

        ref1.clear()
        ref2.clear()

        assertTrue(ref1.equals(ref2), "Both cleared SoftRefs should be equal (both null)")
    }

    @Test
    fun equalsSymmetry() {
        val testObj = TestClass("test")
        val ref1 = SoftRef(testObj)
        val ref2 = SoftRef(testObj)

        assertEquals(ref1.equals(ref2), ref2.equals(ref1), "equals should be symmetric")
    }

    @Test
    fun equalsConsistency() {
        val testObj = TestClass("test")
        val ref1 = SoftRef(testObj)
        val ref2 = SoftRef(testObj)

        val result1 = ref1.equals(ref2)
        val result2 = ref1.equals(ref2)

        assertEquals(result1, result2, "equals should be consistent across calls")
    }

    @Test
    fun notEqualsSoftRefAndWeakRef() {
        val testObj = TestClass("test")
        val softRef = SoftRef(testObj)
        val weakRef = WeakRef(testObj)

        assertFalse(softRef.equals(weakRef), "SoftRef should not equal WeakRef even with same referent")
    }

    // ==================== hashCode tests ====================

    @Test
    fun hashCodeConsistency() {
        val ref = SoftRef(TestClass("test"))
        val hash1 = ref.hashCode()
        val hash2 = ref.hashCode()

        assertEquals(hash1, hash2, "hashCode should be consistent across calls")
    }

    @Test
    fun hashCodeForSameContent() {
        val testObj = TestClass("test")
        val ref1 = SoftRef(testObj)
        val ref2 = SoftRef(testObj)

        assertEquals(ref1.hashCode(), ref2.hashCode(), "Equal objects must have equal hashCodes")
    }

    @Test
    fun hashCodeForDifferentContent() {
        val ref1 = SoftRef(TestClass("test1"))
        val ref2 = SoftRef(TestClass("test2"))

        assertNotEquals(ref1.hashCode(), ref2.hashCode(), "Different referents likely have different hashCodes")
    }

    @Test
    fun hashCodeAfterClear() {
        val ref = SoftRef(TestClass("test"))
        val hashBefore = ref.hashCode()

        ref.clear()

        val hashAfter = ref.hashCode()
        assertEquals(null.hashCode(), hashAfter, "After clearing, hashCode should equal null.hashCode()")
        // hashCode changes because the referent changes, which is correct per equals/hashCode contract
        assertNotEquals(hashBefore, hashAfter, "hashCode should change when referent changes")
    }


    @Test
    fun hashCodeForBothCleared() {
        val ref1 = SoftRef(TestClass("test"))
        val ref2 = SoftRef(TestClass("test"))

        ref1.clear()
        ref2.clear()

        assertEquals(ref1.hashCode(), ref2.hashCode(), "Cleared SoftRefs should have equal hashCodes")
    }

    @Test
    fun hashCodeContractEqualObjectsHaveSameHashCode() {
        val testObj = TestClass("test")
        val ref1 = SoftRef(testObj)
        val ref2 = SoftRef(testObj)

        // If objects are equal, they must have same hashCode
        if (ref1.equals(ref2)) {
            assertEquals(ref1.hashCode(), ref2.hashCode(), "Equal objects must have equal hashCodes")
        }
    }

    @Test
    fun hashCodeCanBeUsedInSet() {
        val testObj = TestClass("test")
        val ref1 = SoftRef(testObj)
        val ref2 = SoftRef(testObj)

        val set = hashSetOf(ref1)
        assertTrue(ref2 in set, "Equal SoftRef should be found in set")
    }

    @Test
    fun hashCodeCanBeUsedInMap() {
        val testObj = TestClass("test")
        val ref = SoftRef(testObj)

        val map = hashMapOf(ref to "value")
        assertEquals("value", map[ref], "SoftRef should work as map key")
    }
}
