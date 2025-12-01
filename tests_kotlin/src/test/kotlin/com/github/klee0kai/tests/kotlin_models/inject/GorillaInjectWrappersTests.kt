package com.github.klee0kai.tests.kotlin_models.inject

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test_kotlin.di.base_forest.RainForestComponent
import com.github.klee0kai.test_kotlin.mowgli.animal.Gorilla
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class GorillaInjectWrappersTests {

    @Test
    fun supportWrappersTest() {
        //Given
        val DI = Stone.createComponent(RainForestComponent::class.java)
        val gorilla = Gorilla()

        //When
        DI.inject(gorilla)

        //Then
        assertNotNull(gorilla.knowledge)
        assertNotNull(gorilla.knowledgeWeakRef.get())
        assertNotNull(gorilla.knowledgeSoftRef.get())
        assertNotNull(gorilla.knowledgeLazyProvide!!.get())
        assertNotNull(gorilla.knowledgePhantomProvide!!.get())
        assertNotNull(gorilla.knowledgePhantomProvide2!!.get())
        assertNotNull(gorilla.knowledgePhantomProvide3!!.get())
    }


    @Test
    fun refWrapperTest() {
        //Given
        val DI = Stone.createComponent(RainForestComponent::class.java)
        val gorilla = Gorilla()

        //When
        DI.inject(gorilla)


        //Then
        assertEquals(
            gorilla.knowledgeWeakRef.get()!!.uuid,
            gorilla.knowledgeWeakRef.get()!!.uuid
        )
        assertEquals(
            gorilla.knowledgeSoftRef.get()!!.uuid,
            gorilla.knowledgeSoftRef.get()!!.uuid
        )
        assertEquals(
            gorilla.knowledgeLazyProvide!!.get()!!.uuid,
            gorilla.knowledgeLazyProvide!!.get()!!.uuid
        )
    }

    @Test
    fun genWrapperTest() {
        //Given
        val DI = Stone.createComponent(RainForestComponent::class.java)
        var gorilla = Gorilla()

        //When
        DI.inject(gorilla)

        //Then
        assertNotEquals(
            gorilla.knowledgePhantomProvide!!.get().uuid,
            gorilla.knowledgePhantomProvide!!.get().uuid
        )
        assertNotEquals(
            gorilla.knowledgePhantomProvide2!!.get().uuid,
            gorilla.knowledgePhantomProvide2!!.get().uuid
        )
        assertNotEquals(
            gorilla.knowledgePhantomProvide3!!.get().uuid,
            gorilla.knowledgePhantomProvide3!!.get().uuid
        )
    }

}