package com.github.klee0kai.stone.test.inject;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.base_forest.ForestComponent;
import com.github.klee0kai.test.mowgli.animal.Mowgli;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class MowgliInjectMethodWrappersTests {

    @Test
    public void supportWrappersTest() {
        //Given
        ForestComponent DI = Stone.createComponent(ForestComponent.class);
        Mowgli mowgli = new Mowgli();


        //When
        DI.inject(mowgli);

        //Then
        assertNotNull(mowgli.methodKnowledgeWeakRef.get());
        assertNotNull(mowgli.methodKnowledgeSoftRef.get());
        assertNotNull(mowgli.methodKnowledgeLazyProvide.get());
        assertNotNull(mowgli.methodKnowledgePhantomProvide.get());
        assertNotNull(mowgli.methodKnowledgePhantomProvide2.get());
        assertNotNull(mowgli.methodKnowledgePhantomProvide3.get());
    }


    @Test
    public void refWrapperTest() {
        //Given
        ForestComponent DI = Stone.createComponent(ForestComponent.class);
        Mowgli mowgli = new Mowgli();

        //When
        DI.inject(mowgli);


        //Then
        assertEquals(
                Objects.requireNonNull(mowgli.methodKnowledgeWeakRef.get()).uuid,
                Objects.requireNonNull(mowgli.methodKnowledgeWeakRef.get()).uuid
        );
        assertEquals(
                Objects.requireNonNull(mowgli.methodKnowledgeSoftRef.get()).uuid,
                Objects.requireNonNull(mowgli.methodKnowledgeSoftRef.get()).uuid
        );
        assertEquals(
                Objects.requireNonNull(mowgli.methodKnowledgeLazyProvide.get()).uuid,
                Objects.requireNonNull(mowgli.methodKnowledgeLazyProvide.get()).uuid
        );
    }

    @Test
    public void genWrapperTest() {
        //Given
        ForestComponent DI = Stone.createComponent(ForestComponent.class);
        Mowgli mowgli = new Mowgli();

        //When
        DI.inject(mowgli);


        //Then
        assertNotEquals(
                mowgli.methodKnowledgePhantomProvide.get().uuid,
                mowgli.methodKnowledgePhantomProvide.get().uuid
        );
        assertNotEquals(
                mowgli.methodKnowledgePhantomProvide2.get().uuid,
                mowgli.methodKnowledgePhantomProvide2.get().uuid
        );
        assertNotEquals(
                mowgli.methodKnowledgePhantomProvide3.get().uuid,
                mowgli.methodKnowledgePhantomProvide3.get().uuid
        );
    }
}
