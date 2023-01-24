package com.github.klee0kai.stone.test.inject;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.base_forest.ForestComponent;
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
        ForestComponent DI = Stone.createComponent(ForestComponent.class);
        Horse horse = new Horse();


        //When
        DI.inject(horse, listener -> {

        });
        WeakReference<History> historyWeakReference = new WeakReference<>(horse.history);
        horse = null;
        System.gc();

        //Then: without protect all not uses should be garbage collected
        assertNull(historyWeakReference.get());

    }

    @Test
    public void withProtectInjectTest() throws InterruptedException {
        // Given
        ForestComponent DI = Stone.createComponent(ForestComponent.class);
        Horse horse = new Horse();

        //When
        DI.inject(horse, listener -> {

        });
        WeakReference<History> historyWeakReference = new WeakReference<>(horse.history);
        DI.protectInjected(horse);
        horse = null;
        DI.gcAll();

        //Then
        assertNotNull(historyWeakReference.get());

        //after protect finished
        Thread.sleep(50);
        DI.gcAll();
        assertNull(historyWeakReference.get());
    }

}
