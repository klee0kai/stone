
package com.dirgub.klee0kai.stone.text_ext.inject;

import com.github.klee0kai.test.mowgli.animal.Horse;
import com.github.klee0kai.test_ext.inject.mowgli.OldForest;
import com.github.klee0kai.test_ext.inject.mowgli.animal.OldHorse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class OldInjectTests {

    @Test
    public void oldHorseInjectTest() {
        // Given
        OldForest forest = new OldForest();
        forest.create();
        forest.old();

        // When
        OldHorse horse = new OldHorse();
        horse.born();

        // Then
        assertNotNull(horse.blood);
        assertNotNull(horse.knowledge);
        assertNotNull(horse.conscience);
        assertNotNull(horse.oldKnowledge);
        assertNotNull(horse.osteoarthritis);
    }


    @Test
    public void simpleProvideProTest() {
        // Given
        OldForest forest = new OldForest();
        forest.create();
        forest.old();

        // When
        OldHorse horse = new OldHorse();
        horse.born();

        // Then
        assertTrue(horse.ideology.isFamilyIdeology());

    }
    @Test
    public void overrideProvideTest() {
        // Given
        OldForest forest = new OldForest();
        forest.create();
        forest.old();

        // When
        Horse horse = new Horse();
        horse.born();

        // Then: new items should generate from new DI component
        assertTrue(horse.knowledge.isOldKnowledge());
    }


    @Test
    public void nonGenCacheTest() {
        // Given
        OldForest forest = new OldForest();
        forest.create();

        // When
        Horse horse = new Horse();
        horse.born();
        // simply connect dynamic feature
        forest.old();
        horse.born();

        // Then
        assertFalse(horse.ideology.isFamilyIdeology());
    }


    @Test
    public void overrideCacheTest() {
        // Given
        OldForest forest = new OldForest();
        forest.create();

        // When
        Horse horse = new Horse();
        horse.born();

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
