package com.github.klee0kai.stone.test.inject;

import com.github.klee0kai.test.mowgli.Forest;
import com.github.klee0kai.test.mowgli.animal.Horse;
import com.github.klee0kai.test.mowgli.community.History;
import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


public class HorseProtectInjectTests {

    @Test
    public void withoutProtectInjectTest() {
        // Given
        Forest forest = new Forest();
        forest.create();

        //When
        Horse horse = new Horse();
        horse.born();
        WeakReference<History> historyWeakReference = new WeakReference<>(horse.history);
        horse = null;
        Forest.DI.gcAll();

        //Then: without protect all not uses should be garbage collected
        assertNull(historyWeakReference.get());

    }

    @Test
    public void withProtectInjectTest() throws InterruptedException {
        // Given
        Forest forest = new Forest();
        forest.create();

        //When
        Horse horse = new Horse();
        horse.born();
        WeakReference<History> historyWeakReference = new WeakReference<>(horse.history);
        Forest.DI.protectInjected(horse);
        horse = null;
        Forest.DI.gcAll();

        //Then
        assertNotNull(historyWeakReference.get());

        //after protect finished
        Thread.sleep(60);
        Forest.DI.gcAll();
        assertNull(historyWeakReference.get());
    }

}
