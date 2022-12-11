
package com.dirgub.klee0kai.stone.text_ext.inject;

import com.github.klee0kai.test.inject.Horse;
import com.github.klee0kai.test_ext.inject.OldForest;
import com.github.klee0kai.test_ext.inject.OldHorse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class OldInjectTests {

    @Test
    public void simpleInjectTest() {
        // create common component for all app
        OldForest forest = new OldForest();
        forest.create();
        forest.old();

        //use sub app components
        OldHorse horse = new OldHorse();
        horse.born();

        //check inject is completed
        assertNotNull(horse.blood);
        assertNotNull(horse.knowledge);
        assertNotNull(horse.conscience);
        assertNotNull(horse.oldKnowledge);
        assertNotNull(horse.osteoarthritis);
    }


    @Test
    public void simpleProvideProTest() {
        OldForest forest = new OldForest();
        forest.create();
        forest.old();

        //use sub app components
        OldHorse horse = new OldHorse();
        horse.born();

        assertTrue(horse.ideology.isFamilyIdeology());

    }
    @Test
    public void overrideProvideTest() {
        OldForest forest = new OldForest();
        forest.create();
        forest.old();

        Horse horse = new Horse();
        horse.born();

        // new items should generate from new DI component
        assertTrue(horse.knowledge.isOldKnowledge());
    }


    @Test
    public void nonGenCacheTest() {
        OldForest forest = new OldForest();
        forest.create();

        Horse horse = new Horse();
        horse.born();

        assertFalse(horse.ideology.isFamilyIdeology());

        // simply connect dynamic feature
        forest.old();
        horse.born();

        assertFalse(horse.ideology.isFamilyIdeology());
    }


    @Test
    public void overrideCacheTest() {
        OldForest forest = new OldForest();
        forest.create();

        Horse horse = new Horse();
        horse.born();

        assertFalse(horse.ideology.isFamilyIdeology());

        // simply connect dynamic feature
        forest.old();
        horse.born();

        //use cached component if not need use overrided
        assertFalse(horse.ideology.isFamilyIdeology());

        OldHorse oldHorse = new OldHorse();
        oldHorse.born();

        //check override on DIPro
        assertTrue(oldHorse.ideology.isFamilyIdeology());

        // all use overrided features
        horse.born();

        assertTrue(horse.ideology.isFamilyIdeology());

    }


}
