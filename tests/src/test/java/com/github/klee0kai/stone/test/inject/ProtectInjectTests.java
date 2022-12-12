package com.github.klee0kai.stone.test.inject;

import com.github.klee0kai.test.inject.Forest;
import com.github.klee0kai.test.inject.Horse;
import com.github.klee0kai.test.inject.School;
import com.github.klee0kai.test.inject.forest.History;
import com.github.klee0kai.test.inject.identity.Knowledge;
import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


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
    public void withProtectInjectTest() throws InterruptedException {
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
        Thread.sleep(60);

        Forest.DI.gcAll();
        assertNull(historyWeakReference.get());
    }

    @Test
    public void ignoreWrappersTest() {
        // create common component for all app
        Forest forest = new Forest();
        forest.create();

        //use sub app components
        School school = new School();
        school.build();

        WeakReference<History> history = new WeakReference<>(school.historyLazyProvide.get());
        WeakReference<Knowledge> knowledge1 = new WeakReference<>(school.knowledgePhantomProvide.get());
        WeakReference<Knowledge> knowledge2 = new WeakReference<>(school.knowledgePhantomProvide2.get());
        WeakReference<Knowledge> knowledge3 = new WeakReference<>(school.knowledgePhantomProvide3.get());

        Forest.DI.protectInjected(school);
        Forest.DI.gcAll();

        assertNotNull(history.get());
        assertNull(knowledge1.get());
        assertNull(knowledge2.get());
        assertNull(knowledge3.get());

    }
}
