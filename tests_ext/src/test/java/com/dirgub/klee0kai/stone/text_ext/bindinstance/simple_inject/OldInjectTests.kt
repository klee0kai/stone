package com.dirgub.klee0kai.stone.text_ext.bindinstance.simple_inject

import com.github.klee0kai.test.di.base_forest.ForestComponent
import com.github.klee0kai.test.di.base_forest.ForestComponentStoneComponent
import com.github.klee0kai.test.mowgli.animal.Horse
import com.github.klee0kai.test_ext.inject.di.forest.OldForestComponent
import com.github.klee0kai.test_ext.inject.di.forest.OldForestComponentStoneComponent
import com.github.klee0kai.test_ext.inject.mowgli.animal.OldHorse
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull

class OldInjectTests {

    @Test
    fun oldHorseInjectTest() {
        // Given
        val DI = ForestComponentStoneComponent()
        val DIPro = OldForestComponentStoneComponent()
        DIPro.extOf(DI)
        val horse = OldHorse()

        // When
        DIPro.inject(horse)

        // Then
        assertNotNull(horse.blood)
        assertNotNull(horse.knowledge)
        assertNotNull(horse.conscience)
        assertNotNull(horse.oldKnowledge)
        assertNotNull(horse.osteoarthritis)
    }


    @Test
    fun simpleProvideProTest() {
        // Given
        val DI = ForestComponentStoneComponent()
        val DIPro = OldForestComponentStoneComponent()
        DIPro.extOf(DI)
        val horse = OldHorse()

        // When
        DIPro.inject(horse)

        // Then
        assertTrue(horse.ideology!!.isFamilyIdeology)
    }

    @Test
    fun overrideProvideTest() {
        // Given
        val DI = ForestComponentStoneComponent()
        val DIPro = OldForestComponentStoneComponent()
        DIPro.extOf(DI)
        val horse = OldHorse()

        // When
        DIPro.inject(horse)

        // Then: new items should generate from new DI component
        assertTrue(horse.knowledge!!.isOldKnowledge)
    }


    @Test
    fun nonGenCacheTest() {
        // Given
        val DI: ForestComponent = ForestComponentStoneComponent()
        val horse = Horse()

        // When
        DI.inject(horse)

        // simply connect dynamic feature
        val DIPro: OldForestComponent = OldForestComponentStoneComponent()
        DIPro.extOf(DI)
        DI.inject(horse)

        // Then
        assertFalse(horse.ideology!!.isFamilyIdeology)
    }


    @Test
    fun overrideCacheTest() {
        // Given
        val DI: ForestComponent = ForestComponentStoneComponent()
        val horse = Horse()

        // When
        DI.inject(horse)

        // simply connect dynamic feature
        val DIPro: OldForestComponent = OldForestComponentStoneComponent()
        DIPro.extOf(DI)
        DI.inject(horse)

        //use cached component if not need use overrided
        assertFalse(horse.ideology!!.isFamilyIdeology)

        val oldHorse = OldHorse()
        DIPro.inject(oldHorse)

        //check override on DIPro
        assertTrue(oldHorse.ideology!!.isFamilyIdeology)

        // all use overrided features
        DI.inject(horse)

        assertTrue(horse.ideology!!.isFamilyIdeology)
    }

}
