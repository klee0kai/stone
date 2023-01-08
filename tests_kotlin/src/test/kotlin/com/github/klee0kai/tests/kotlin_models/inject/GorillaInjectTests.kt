package com.github.klee0kai.tests.kotlin_models.inject

import com.github.klee0kai.test_kotlin.mowgli.RainForest
import com.github.klee0kai.test_kotlin.mowgli.animal.Cougar
import com.github.klee0kai.test_kotlin.mowgli.animal.Gorilla
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class GorillaInjectTests {

    @Test
    fun gorillaBornTest() {
        //Given
        val forest = RainForest()
        forest.create()

        //When
        val gorilla = Gorilla()
        gorilla.born()

        //Then
        assertNotNull(gorilla.blood)
        assertNotNull(gorilla.knowledge)
        assertNotNull(gorilla.conscience)
    }

    @Test
    fun oneBloodTest() {
        //Given
        val forest = RainForest()
        forest.create()

        //When
        val gorilla = Gorilla()
        gorilla.born()
        val cougar = Cougar()
        cougar.born()


        //Then
        assertEquals(
            cougar.blood!!.uuid,
            gorilla.blood!!.uuid
        )
    }

    @Test
    fun personalityTest() {
        //Given
        val forest = RainForest()
        forest.create()

        //When
        val gorilla = Gorilla()
        gorilla.born()
        val cougar = Cougar()
        cougar.born()

        //Then
        assertNotEquals(
            gorilla.conscience!!.uuid,
            cougar.conscience!!.uuid
        )
    }
}