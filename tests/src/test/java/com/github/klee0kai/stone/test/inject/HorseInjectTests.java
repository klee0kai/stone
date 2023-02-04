package com.github.klee0kai.stone.test.inject;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.base_forest.ForestComponent;
import com.github.klee0kai.test.mowgli.animal.Horse;
import com.github.klee0kai.test.mowgli.animal.Mowgli;
import com.github.klee0kai.test.mowgli.animal.Snake;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class HorseInjectTests {

    @Test
    public void horseBornTest() {
        //Given
        ForestComponent DI = Stone.createComponent(ForestComponent.class);
        Horse horse = new Horse();


        //When
        DI.inject(horse, listener -> {

        });

        //Then
        assertNotNull(horse.blood);
        assertNotNull(horse.knowledge);
        assertNotNull(horse.conscience);
    }


    @Test
    public void mowgliBornTest() {
        //Given
        ForestComponent DI = Stone.createComponent(ForestComponent.class);
        Mowgli mowgli = new Mowgli();


        //When
        DI.inject(mowgli);

        //Then
        assertNotNull(mowgli.blood);
        assertNotNull(mowgli.knowledge);
        assertNotNull(mowgli.conscience);
    }

    @Test
    public void oneBloodTest() {
        //Given
        ForestComponent DI = Stone.createComponent(ForestComponent.class);
        Mowgli mowgli = new Mowgli();
        Snake snake = new Snake();


        //When
        DI.inject(mowgli);
        DI.inject(snake);

        //Then
        assertEquals(mowgli.blood.uuid, snake.blood.uuid);
    }

    @Test
    public void personalityTest() {
        //Given
        ForestComponent DI = Stone.createComponent(ForestComponent.class);
        Mowgli mowgli = new Mowgli();
        Snake snake = new Snake();


        //When
        DI.inject(mowgli);
        DI.inject(snake);

        //Then
        assertNotEquals(mowgli.conscience.uuid, snake.conscience.uuid);
    }

}
