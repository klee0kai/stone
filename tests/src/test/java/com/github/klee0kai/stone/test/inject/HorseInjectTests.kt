package com.github.klee0kai.stone.test.inject

import com.github.klee0kai.stone.lifecycle.StoneLifeCycleOwner
import com.github.klee0kai.test.di.base_forest.ForestComponentStoneComponent
import com.github.klee0kai.test.mowgli.animal.Horse
import com.github.klee0kai.test.mowgli.animal.Mowgli
import com.github.klee0kai.test.mowgli.animal.Snake
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull

class HorseInjectTests {

    @Test
    fun horseBornTest() {
        //Given
        val DI = ForestComponentStoneComponent()
        val horse = Horse()


        //When
        DI.inject(
            horse,
            stoneLifeCycleOwner = StoneLifeCycleOwner { listener -> }
        )

        //Then
        assertNotNull(horse.blood)
        assertNotNull(horse.knowledge)
        assertNotNull(horse.conscience)
        assertNotNull(horse.methodInjectedConscience)
        assertNotNull(horse.methodInjectedKnowledge)
    }

    @Test
    fun mowgliBornTest() {
        //Given
        val DI = ForestComponentStoneComponent()
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
        val DI = ForestComponentStoneComponent()
        val mowgli = Mowgli()
        val snake = Snake()


        //When
        DI.inject(mowgli)
        DI.inject(snake)

        //Then
        assertEquals(
            mowgli.blood!!.uuid,
            snake.blood!!.uuid
        )
    }

    @Test
    fun personalityTest() {
        //Given
        val DI = ForestComponentStoneComponent()
        val mowgli = Mowgli()
        val snake = Snake()


        //When
        DI.inject(mowgli)
        DI.inject(snake)

        //Then
        assertNotEquals(
            mowgli.conscience!!.uuid,
            snake.conscience!!.uuid,
        )
    }
}
