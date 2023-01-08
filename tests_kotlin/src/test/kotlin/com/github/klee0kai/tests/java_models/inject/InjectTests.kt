package com.github.klee0kai.tests.java_models.inject

import com.github.klee0kai.test.mowgli.Forest
import com.github.klee0kai.test.mowgli.animal.Horse
import com.github.klee0kai.test.mowgli.animal.Mowgli
import com.github.klee0kai.test.mowgli.animal.Snake
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class InjectTests {
    @Test
    fun horseBornTest() {
        //Given
        val forest = Forest()
        forest.create()

        //When
        val horse = Horse()
        horse.born()

        //Then
        assertNotNull(horse.blood)
        assertNotNull(horse.knowledge)
        assertNotNull(horse.conscience)
    }

    @Test
    fun mowgliBornTest() {
        //Given
        val forest = Forest()
        forest.create()

        //When
        val mowgli = Mowgli()
        mowgli.born()

        //Then
        assertNotNull(mowgli.blood)
        assertNotNull(mowgli.knowledge)
        assertNotNull(mowgli.conscience)
    }

    @Test
    fun oneBloodTest() {
        //Given
        val forest = Forest()
        forest.create()

        //When
        val mowgli = Mowgli()
        mowgli.born()
        val snake = Snake()
        snake.born()


        //Then
        assertEquals(mowgli.blood.uuid, snake.blood.uuid)
    }

    @Test
    fun personalityTest() {
        //Given
        val forest = Forest()
        forest.create()

        //When
        val mowgli = Mowgli()
        mowgli.born()
        val snake = Snake()
        snake.born()

        //Then
        assertNotEquals(mowgli.conscience.uuid, snake.conscience.uuid)
    }
}