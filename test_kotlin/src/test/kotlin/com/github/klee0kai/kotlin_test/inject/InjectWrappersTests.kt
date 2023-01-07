package com.github.klee0kai.kotlin_test.inject

import com.github.klee0kai.test_kotlin.mowgli.RainForest
import com.github.klee0kai.test_kotlin.mowgli.animal.Gorilla
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class InjectWrappersTests {

    @Test
    fun supportWrappersTest() {
        //Given
        val forest = RainForest()
        forest.create()

        //When
        val gorilla = Gorilla()
        gorilla.born()

        //Then
        Assertions.assertNotNull(gorilla.knowledge)
        Assertions.assertNotNull(gorilla.knowledgeWeakRef.get())
        Assertions.assertNotNull(gorilla.knowledgeSoftRef.get())
        Assertions.assertNotNull(gorilla.knowledgeLazyProvide!!.get())
        Assertions.assertNotNull(gorilla.knowledgePhantomProvide!!.get())
        Assertions.assertNotNull(gorilla.knowledgePhantomProvide2!!.get())
        Assertions.assertNotNull(gorilla.knowledgePhantomProvide3!!.get())
    }


    @Test
    fun refWrapperTest() {
        //Given
        val forest = RainForest()
        forest.create()

        //When
        val gorilla = Gorilla()
        gorilla.born()


        //Then
        Assertions.assertEquals(
            gorilla.knowledgeWeakRef.get()!!.uuid,
            gorilla.knowledgeWeakRef.get()!!.uuid
        )
        Assertions.assertEquals(
            gorilla.knowledgeSoftRef.get()!!.uuid,
            gorilla.knowledgeSoftRef.get()!!.uuid
        )
        Assertions.assertEquals(
            gorilla.knowledgeLazyProvide!!.get()!!.uuid,
            gorilla.knowledgeLazyProvide!!.get()!!.uuid
        )
    }

    @Test
    fun genWrapperTest() {
        //Given
        val forest = RainForest()
        forest.create()

        //When
        val gorilla = Gorilla()
        gorilla.born()

        //Then
        Assertions.assertNotEquals(
            gorilla.knowledgePhantomProvide!!.get().uuid,
            gorilla.knowledgePhantomProvide!!.get().uuid
        )
        Assertions.assertNotEquals(
            gorilla.knowledgePhantomProvide2!!.get().uuid,
            gorilla.knowledgePhantomProvide2!!.get().uuid
        )
        Assertions.assertNotEquals(
            gorilla.knowledgePhantomProvide3!!.get().uuid,
            gorilla.knowledgePhantomProvide3!!.get().uuid
        )
    }

}