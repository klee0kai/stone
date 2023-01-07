package com.github.klee0kai.stone.test.inject;

import com.github.klee0kai.test.mowgli.Forest;
import com.github.klee0kai.test.mowgli.animal.Horse;
import com.github.klee0kai.test.mowgli.animal.Mowgli;
import com.github.klee0kai.test.mowgli.animal.Snake;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class InjectTests {

    @Test
    public void horseBornTest() {
        //Given
        Forest forest = new Forest();
        forest.create();

        //When
        Horse horse = new Horse();
        horse.born();

        //Then
        assertNotNull(horse.blood);
        assertNotNull(horse.knowledge);
        assertNotNull(horse.conscience);
    }


    @Test
    public void mowgliBornTest() {
        //Given
        Forest forest = new Forest();
        forest.create();

        //When
        Mowgli mowgli = new Mowgli();
        mowgli.born();

        //Then
        assertNotNull(mowgli.blood);
        assertNotNull(mowgli.knowledge);
        assertNotNull(mowgli.conscience);
    }

    @Test
    public void oneBloodTest() {
        //Given
        Forest forest = new Forest();
        forest.create();

        //When
        Mowgli mowgli = new Mowgli();
        mowgli.born();
        Snake snake = new Snake();
        snake.born();


        //Then
        assertEquals(mowgli.blood.uuid, snake.blood.uuid);
    }

    @Test
    public void personalityTest() {
        //Given
        Forest forest = new Forest();
        forest.create();

        //When
        Mowgli mowgli = new Mowgli();
        mowgli.born();
        Snake snake = new Snake();
        snake.born();

        //Then
        assertNotEquals(mowgli.conscience.uuid, snake.conscience.uuid);
    }

}
