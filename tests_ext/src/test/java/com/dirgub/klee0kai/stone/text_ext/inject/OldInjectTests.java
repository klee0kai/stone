
package com.dirgub.klee0kai.stone.text_ext.inject;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.base_forest.ForestComponent;
import com.github.klee0kai.test.mowgli.animal.Horse;
import com.github.klee0kai.test_ext.inject.di.forest.OldForestComponent;
import com.github.klee0kai.test_ext.inject.mowgli.animal.OldHorse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class OldInjectTests {

    @Test
    public void oldHorseInjectTest() {
        // Given
        ForestComponent DI = Stone.createComponent(ForestComponent.class);
        OldForestComponent DIPro = Stone.createComponent(OldForestComponent.class);
        DIPro.extOf(DI);
        OldHorse horse = new OldHorse();

        // When
        DIPro.inject(horse);

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
        ForestComponent DI = Stone.createComponent(ForestComponent.class);
        OldForestComponent DIPro = Stone.createComponent(OldForestComponent.class);
        DIPro.extOf(DI);
        OldHorse horse = new OldHorse();

        // When
        DIPro.inject(horse);

        // Then
        assertTrue(horse.ideology.isFamilyIdeology());

    }

    @Test
    public void overrideProvideTest() {
        // Given
        ForestComponent DI = Stone.createComponent(ForestComponent.class);
        OldForestComponent DIPro = Stone.createComponent(OldForestComponent.class);
        DIPro.extOf(DI);
        OldHorse horse = new OldHorse();

        // When
        DIPro.inject(horse);

        // Then: new items should generate from new DI component
        assertTrue(horse.knowledge.isOldKnowledge());
    }


    @Test
    public void nonGenCacheTest() {
        // Given
        ForestComponent DI = Stone.createComponent(ForestComponent.class);
        Horse horse = new Horse();

        // When
        DI.inject(horse);

        // simply connect dynamic feature
        OldForestComponent DIPro = Stone.createComponent(OldForestComponent.class);
        DIPro.extOf(DI);
        DI.inject(horse);

        // Then.
        assertFalse(horse.ideology.isFamilyIdeology());
    }


    @Test
    public void overrideCacheTest() {
        // Given
        ForestComponent DI = Stone.createComponent(ForestComponent.class);
        Horse horse = new Horse();

        // When
        DI.inject(horse);

        // simply connect dynamic feature
        OldForestComponent DIPro = Stone.createComponent(OldForestComponent.class);
        DIPro.extOf(DI);
        DI.inject(horse);

        //use cached component if not need use overrided
        assertFalse(horse.ideology.isFamilyIdeology());

        OldHorse oldHorse = new OldHorse();
        DIPro.inject(oldHorse);

        //check override on DIPro
        assertTrue(oldHorse.ideology.isFamilyIdeology());

        // all use overrided features
        DI.inject(horse);

        assertTrue(horse.ideology.isFamilyIdeology());

    }


}
