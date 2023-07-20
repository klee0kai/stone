package com.github.klee0kai.tests.java_models.inject

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.stone.lifecycle.StoneLifeCycleListener
import com.github.klee0kai.test.di.base_forest.ForestComponent
import com.github.klee0kai.test.mowgli.animal.Horse
import com.github.klee0kai.test.mowgli.animal.Mowgli
import com.github.klee0kai.test.mowgli.animal.Snake
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class HorseInjectTests {
    @Test
    fun horseBornTest() {
        //Given
        val DI = Stone.createComponent(ForestComponent::class.java)
        val horse = Horse()


        //When
        DI.inject(horse) { listener: StoneLifeCycleListener? -> }

        //Then
        assertNotNull(horse.blood)
        assertNotNull(horse.knowledge)
        assertNotNull(horse.conscience)
    }

    @Test
    fun mowgliBornTest() {
        //Given
        val DI = Stone.createComponent(ForestComponent::class.java)
        val mowgli = Mowgli()


        //When
        DI.inject(mowgli)

        //Then
        assertNotNull(mowgli.blood)
        assertNotNull(mowgli.knowledge)
        assertNotNull(mowgli.conscience)
    }

    @Test
    fun oneBloodTest() {
        //Given
        val DI = Stone.createComponent(ForestComponent::class.java)
        val mowgli = Mowgli()
        val snake = Snake()


        //When
        DI.inject(mowgli)
        DI.inject(snake)

        //Then
        assertEquals(mowgli.blood.uuid, snake.blood.uuid)
    }

    @Test
    fun personalityTest() {
        //Given
        val DI = Stone.createComponent(ForestComponent::class.java)
        val mowgli = Mowgli()
        val snake = Snake()


        //When
        DI.inject(mowgli)
        DI.inject(snake)

        //Then
        assertNotEquals(mowgli.conscience.uuid, snake.conscience.uuid)
    }
}