package com.github.klee0kai.stone.test.inject;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.base_forest.ForestComponent;
import com.github.klee0kai.test.mowgli.animal.Mowgli;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class MowgliInjectWrappersTests {

    @Test
    public void supportWrappersTest() {
        //Given
        ForestComponent DI = Stone.createComponent(ForestComponent.class);
        Mowgli mowgli = new Mowgli();


        //When
        DI.inject(mowgli);

        //Then
        assertNotNull(mowgli.knowledge);
        assertNotNull(mowgli.knowledgeWeakRef.get());
        assertNotNull(mowgli.knowledgeSoftRef.get());
        assertNotNull(mowgli.knowledgeLazyProvide.get());
        assertNotNull(mowgli.knowledgePhantomProvide.get());
        assertNotNull(mowgli.knowledgePhantomProvide2.get());
        assertNotNull(mowgli.knowledgePhantomProvide3.get());
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
                Objects.requireNonNull(mowgli.knowledgeWeakRef.get()).uuid,
                Objects.requireNonNull(mowgli.knowledgeWeakRef.get()).uuid
        );
        assertEquals(
                Objects.requireNonNull(mowgli.knowledgeSoftRef.get()).uuid,
                Objects.requireNonNull(mowgli.knowledgeSoftRef.get()).uuid
        );
        assertEquals(
                Objects.requireNonNull(mowgli.knowledgeLazyProvide.get()).uuid,
                Objects.requireNonNull(mowgli.knowledgeLazyProvide.get()).uuid
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
                mowgli.knowledgePhantomProvide.get().uuid,
                mowgli.knowledgePhantomProvide.get().uuid
        );
        assertNotEquals(
                mowgli.knowledgePhantomProvide2.get().uuid,
                mowgli.knowledgePhantomProvide2.get().uuid
        );
        assertNotEquals(
                mowgli.knowledgePhantomProvide3.get().uuid,
                mowgli.knowledgePhantomProvide3.get().uuid
        );
    }
}
