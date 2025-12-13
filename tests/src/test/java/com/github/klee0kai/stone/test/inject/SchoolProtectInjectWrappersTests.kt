package com.github.klee0kai.stone.test.inject;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.base_forest.ForestComponent;
import com.github.klee0kai.test.mowgli.School;
import com.github.klee0kai.test.mowgli.community.History;
import com.github.klee0kai.test.mowgli.identity.Knowledge;
import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


public class SchoolProtectInjectWrappersTests {

    @Test
    public void lazyWrapperProtectTest() {
        // Given
        ForestComponent DI = Stone.createComponent(ForestComponent.class);
        School school = new School();

        //When
        DI.inject(school);

        WeakReference<History> history = new WeakReference<>(school.historyLazyProvide.get());
        DI.protectInjected(school);
        DI.gcAll();

        //Then
        assertNotNull(history.get());
    }

    @Test
    public void ignoreProvideWrapperProtectTest() {
        // Given
        ForestComponent DI = Stone.createComponent(ForestComponent.class);
        School school = new School();

        //When
        DI.inject(school);
        WeakReference<Knowledge> knowledge1 = new WeakReference<>(school.knowledgePhantomProvide.get());
        WeakReference<Knowledge> knowledge2 = new WeakReference<>(school.knowledgePhantomProvide2.get());
        WeakReference<Knowledge> knowledge3 = new WeakReference<>(school.knowledgePhantomProvide3.get());
        DI.protectInjected(school);
        DI.gcAll();

        //Then
        assertNull(knowledge1.get());
        assertNull(knowledge2.get());
        assertNull(knowledge3.get());

    }
}
