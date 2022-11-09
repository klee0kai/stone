package com.github.klee0kai.stone.test.inject;

import com.github.klee0kai.test.inject.Forest;
import com.github.klee0kai.test.inject.Horse;
import com.github.klee0kai.test.inject.Mowgli;
import com.github.klee0kai.test.inject.Snake;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class InjectTests {

    @Test
    public void simpleInjectTest() {
        // create common component for all app
        Forest forest = new Forest();
        forest.create();

        //use sub app components
        Horse horse = new Horse();
        horse.born();
        Snake snake = new Snake();
        snake.born();
        Mowgli mowgli = new Mowgli();
        mowgli.born();

        //check inject is completed
        assertNotNull(horse.blood);
        assertNotNull(horse.knowledge);
        assertNotNull(horse.conscience);
        assertNotNull(horse.knowledge);
        assertNotNull(snake.blood);
        assertNotNull(snake.knowledge);
        assertNotNull(snake.conscience);
        assertNotNull(snake.knowledge);
        assertNotNull(mowgli.blood);
        assertNotNull(mowgli.knowledge);
        assertNotNull(mowgli.conscience);
        assertNotNull(mowgli.knowledge);

        //check cached items
        assertEquals(horse.blood.uuid, snake.blood.uuid);
        assertEquals(horse.blood.uuid, mowgli.blood.uuid);
        assertEquals(horse.earth.uuid, snake.earth.uuid);
        assertEquals(horse.earth.uuid, mowgli.earth.uuid);

        //check factory items
        assertNotEquals(horse.conscience.uuid, snake.conscience.uuid);
        assertNotEquals(horse.conscience.uuid, mowgli.conscience.uuid);
        assertNotEquals(horse.knowledge.uuid, snake.knowledge.uuid);
        assertNotEquals(horse.knowledge.uuid, mowgli.knowledge.uuid);

    }
}
