package com.github.klee0kai.tests.kotlin_models.inject

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test_kotlin.di.base_forest.RainForestComponent
import com.github.klee0kai.test_kotlin.mowgli.animal.Cougar
import com.github.klee0kai.test_kotlin.mowgli.animal.Gorilla
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class GorillaInjectTests {

    @Test
    fun gorillaBornTest() {
        //Given
        val DI = Stone.createComponent(RainForestComponent::class.java)
        val gorilla = Gorilla()


        //When
        DI.inject(gorilla)

        //Then
        assertNotNull(gorilla.blood)
        assertNotNull(gorilla.knowledge)
        assertNotNull(gorilla.conscience)
    }

    @Test
    fun oneBloodTest() {
        //Given
        val DI = Stone.createComponent(RainForestComponent::class.java)
        val gorilla = Gorilla()
        val cougar = Cougar()


        //When
        DI.inject(gorilla)
        DI.inject(cougar)


        //Then
        assertEquals(
            cougar.blood!!.uuid,
            gorilla.blood!!.uuid
        )
    }

    @Test
    fun personalityTest() {
        //Given
        val DI = Stone.createComponent(RainForestComponent::class.java)
        val gorilla = Gorilla()
        val cougar = Cougar()


        //When
        DI.inject(gorilla)
        DI.inject(cougar)

        //Then
        assertNotEquals(
            gorilla.conscience!!.uuid,
            cougar.conscience!!.uuid
        )
    }
}