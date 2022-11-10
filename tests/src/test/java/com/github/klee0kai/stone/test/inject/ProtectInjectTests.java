package com.github.klee0kai.stone.test.inject;

import com.github.klee0kai.test.inject.Forest;
import com.github.klee0kai.test.inject.Horse;
import com.github.klee0kai.test.inject.Mowgli;
import com.github.klee0kai.test.inject.Snake;
import com.github.klee0kai.test.inject.forest.History;
import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;

import static org.junit.jupiter.api.Assertions.*;


public class ProtectInjectTests {

    @Test
    public void withoutProtectInjectTest() {
        // create common component for all app
        Forest forest = new Forest();
        forest.create();

        //use sub app components
        Horse horse = new Horse();
        horse.born();

        WeakReference<History> historyWeakReference = new WeakReference<>(horse.history);

        //check inject is completed
        assertNotNull(horse.blood);
        assertNotNull(horse.knowledge);
        assertNotNull(horse.conscience);
        assertNotNull(horse.history);

        //without protect all not uses should be garbage collected
        horse = null;
        Forest.DI.gcAll();
        assertNull(historyWeakReference.get());

    }

    @Test
    public void withProtectInjectTest() {
        // create common component for all app
        Forest forest = new Forest();
        forest.create();

        //use sub app components
        Horse horse = new Horse();
        horse.born();

        WeakReference<History> historyWeakReference = new WeakReference<>(horse.history);

        //check inject is completed
        assertNotNull(horse.blood);
        assertNotNull(horse.knowledge);
        assertNotNull(horse.conscience);
        assertNotNull(horse.history);

        //without protect all not uses should be garbage collected
        Forest.DI.protectInjected(horse);
        horse = null;
        Forest.DI.gcAll();
        assertNotNull(historyWeakReference.get());

        //after protect finished
        try {
            Thread.sleep(60);
        } catch (InterruptedException e) {
        }
        Forest.DI.gcAll();
        assertNull(historyWeakReference.get());


    }
}
