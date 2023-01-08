package com.github.klee0kai.tests.java_models.inject

import com.github.klee0kai.test.mowgli.Forest
import com.github.klee0kai.test.mowgli.animal.Mowgli
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MowgliInjectWrappersTests {
    @Test
    fun supportWrappersTest() {
        //Given
        val forest = Forest()
        forest.create()

        //When
        val mowgli = Mowgli()
        mowgli.born()

        //Then
        assertNotNull(mowgli.knowledge)
        assertNotNull(mowgli.knowledgeWeakRef.get())
        assertNotNull(mowgli.knowledgeSoftRef.get())
        assertNotNull(mowgli.knowledgeLazyProvide.get())
        assertNotNull(mowgli.knowledgePhantomProvide.get())
        assertNotNull(mowgli.knowledgePhantomProvide2.get())
        assertNotNull(mowgli.knowledgePhantomProvide3.get())
    }

    @Test
    fun refWrapperTest() {
        //Given
        val forest = Forest()
        forest.create()

        //When
        val mowgli = Mowgli()
        mowgli.born()

        //Then
        assertEquals(
            mowgli.knowledgeWeakRef.get()!!.uuid,
            mowgli.knowledgeWeakRef.get()!!.uuid
        )
        assertEquals(
            mowgli.knowledgeSoftRef.get()!!.uuid,
            mowgli.knowledgeSoftRef.get()!!.uuid
        )
        assertEquals(
            mowgli.knowledgeLazyProvide.get()!!.uuid,
            mowgli.knowledgeLazyProvide.get()!!.uuid
        )
    }

    @Test
    fun genWrapperTest() {
        //Given
        val forest = Forest()
        forest.create()

        //When
        val mowgli = Mowgli()
        mowgli.born()

        //Then
        assertNotEquals(
            mowgli.knowledgePhantomProvide.get().uuid,
            mowgli.knowledgePhantomProvide.get().uuid
        )
        assertNotEquals(
            mowgli.knowledgePhantomProvide2.get().uuid,
            mowgli.knowledgePhantomProvide2.get().uuid
        )
        assertNotEquals(
            mowgli.knowledgePhantomProvide3.get().uuid,
            mowgli.knowledgePhantomProvide3.get().uuid
        )
    }
}