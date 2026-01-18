package com.github.klee0kai.stone.test.inject

import com.github.klee0kai.test.di.base_forest.ForestComponentStoneComponent
import com.github.klee0kai.test.mowgli.animal.Mowgli
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MowgliInjectWrappersTests {

    @Test
    fun supportWrappersTest() {
        //Given
        val DI = ForestComponentStoneComponent()
        val mowgli = Mowgli()


        //When
        DI.inject(mowgli)

        //Then
        assertNotNull(mowgli.knowledge)
        assertNotNull(mowgli.knowledgeWeakRef!!.get())
        assertNotNull(mowgli.knowledgeSoftRef!!.get())
        assertNotNull(mowgli.knowledgeLazyProvide!!.get())
        assertNotNull(mowgli.knowledgePhantomProvide!!.get())
        assertNotNull(mowgli.knowledgePhantomProvide2!!.get())
        assertNotNull(mowgli.knowledgePhantomProvide3!!.get())
    }

    @Test
    fun refWrapperTest() {
        //Given
        val DI = ForestComponentStoneComponent()
        val mowgli = Mowgli()

        //When
        DI.inject(mowgli)


        //Then
        assertEquals(
            mowgli.knowledgeWeakRef!!.get()!!.uuid,
            mowgli.knowledgeWeakRef!!.get()!!.uuid,
        )
        assertEquals(
            mowgli.knowledgeSoftRef!!.get()!!.uuid,
            mowgli.knowledgeSoftRef!!.get()!!.uuid,
        )
        assertEquals(
            mowgli.knowledgeLazyProvide!!.get()!!.uuid,
            mowgli.knowledgeLazyProvide!!.get()!!.uuid,
        )
    }

    @Test
    fun genWrapperTest() {
        //Given
        val DI = ForestComponentStoneComponent()
        val mowgli = Mowgli()

        //When
        DI.inject(mowgli)

        //Then
        assertNotEquals(
            mowgli.knowledgePhantomProvide!!.get()!!.uuid,
            mowgli.knowledgePhantomProvide!!.get()!!.uuid,
        )
        assertNotEquals(
            mowgli.knowledgePhantomProvide2!!.get()!!.uuid,
            mowgli.knowledgePhantomProvide2!!.get()!!.uuid,
        )
        assertNotEquals(
            mowgli.knowledgePhantomProvide3!!.get()!!.uuid,
            mowgli.knowledgePhantomProvide3!!.get()!!.uuid,
        )
    }
}
