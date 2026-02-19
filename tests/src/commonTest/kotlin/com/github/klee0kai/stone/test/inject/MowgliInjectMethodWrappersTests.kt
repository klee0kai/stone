package com.github.klee0kai.stone.test.inject

import com.github.klee0kai.test.di.base_forest.ForestComponentStoneComponent
import com.github.klee0kai.test.mowgli.animal.Mowgli
import org.junit.jupiter.api.Assertions.assertNotEquals
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

class MowgliInjectMethodWrappersTests {

    @Test
    fun supportWrappersTest() {
        //Given
        val DI = ForestComponentStoneComponent()
        val mowgli = Mowgli()


        //When
        DI.inject(mowgli)

        //Then
        assertNotNull(mowgli.methodKnowledgeWeakRef!!.get())
        assertNotNull(mowgli.methodKnowledgeSoftRef!!.get())
        assertNotNull(mowgli.methodKnowledgeLazyProvide!!.get())
        assertNotNull(mowgli.methodKnowledgePhantomProvide!!.get())
        assertNotNull(mowgli.methodKnowledgePhantomProvide2!!.get())
        assertNotNull(mowgli.methodKnowledgePhantomProvide3!!.get())
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
            mowgli.methodKnowledgeWeakRef!!.get()!!.uuid,
            mowgli.methodKnowledgeWeakRef!!.get()!!.uuid,
        )
        assertEquals(
            mowgli.methodKnowledgeSoftRef!!.get()!!.uuid,
            mowgli.methodKnowledgeSoftRef!!.get()!!.uuid
        )
        assertEquals(
            mowgli.methodKnowledgeLazyProvide!!.get()!!.uuid,
            mowgli.methodKnowledgeLazyProvide!!.get()!!.uuid,
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
            mowgli.methodKnowledgePhantomProvide!!.get()!!.uuid,
            mowgli.methodKnowledgePhantomProvide!!.get()!!.uuid,
        )
        assertNotEquals(
            mowgli.methodKnowledgePhantomProvide2!!.get()!!.uuid,
            mowgli.methodKnowledgePhantomProvide2!!.get()!!.uuid,
        )
        assertNotEquals(
            mowgli.methodKnowledgePhantomProvide3!!.get()!!.uuid,
            mowgli.methodKnowledgePhantomProvide3!!.get()!!.uuid,
        )
    }

}
