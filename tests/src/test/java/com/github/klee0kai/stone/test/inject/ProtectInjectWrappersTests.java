package com.github.klee0kai.stone.test.inject;

import com.github.klee0kai.test.mowgli.Forest;
import com.github.klee0kai.test.mowgli.School;
import com.github.klee0kai.test.mowgli.identity.Knowledge;
import com.github.klee0kai.test.mowgli.world.History;
import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


public class ProtectInjectWrappersTests {

    @Test
    public void lazyWrapperProtectTest() {
        // Given
        Forest forest = new Forest();
        forest.create();

        //When
        School school = new School();
        school.build();
        WeakReference<History> history = new WeakReference<>(school.historyLazyProvide.get());
        Forest.DI.protectInjected(school);
        Forest.DI.gcAll();

        //Then
        assertNotNull(history.get());
    }

    @Test
    public void ignoreProvideWrapperProtectTest() {
        // Given
        Forest forest = new Forest();
        forest.create();

        //When
        School school = new School();
        school.build();
        WeakReference<Knowledge> knowledge1 = new WeakReference<>(school.knowledgePhantomProvide.get());
        WeakReference<Knowledge> knowledge2 = new WeakReference<>(school.knowledgePhantomProvide2.get());
        WeakReference<Knowledge> knowledge3 = new WeakReference<>(school.knowledgePhantomProvide3.get());
        Forest.DI.protectInjected(school);
        Forest.DI.gcAll();

        //Then
        assertNull(knowledge1.get());
        assertNull(knowledge2.get());
        assertNull(knowledge3.get());

    }
}
