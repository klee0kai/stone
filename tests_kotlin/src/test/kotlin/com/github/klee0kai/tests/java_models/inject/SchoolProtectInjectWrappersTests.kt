package com.github.klee0kai.tests.java_models.inject

import com.github.klee0kai.test.mowgli.Forest
import com.github.klee0kai.test.mowgli.School
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.lang.ref.WeakReference

class SchoolProtectInjectWrappersTests {
    @Test
    fun lazyWrapperProtectTest() {
        // Given
        val forest = Forest()
        forest.create()

        //When
        val school = School()
        school.build()
        val history = WeakReference(school.historyLazyProvide.get())
        Forest.DI.protectInjected(school)
        Forest.DI.gcAll()

        //Then
        assertNotNull(history.get())
    }

    @Test
    fun ignoreProvideWrapperProtectTest() {
        // Given
        val forest = Forest()
        forest.create()

        //When
        val school = School()
        school.build()
        val knowledge1 = WeakReference(school.knowledgePhantomProvide.get())
        val knowledge2 = WeakReference(school.knowledgePhantomProvide2.get())
        val knowledge3 = WeakReference(school.knowledgePhantomProvide3.get())
        Forest.DI.protectInjected(school)
        Forest.DI.gcAll()

        //Then
        assertNull(knowledge1.get())
        assertNull(knowledge2.get())
        assertNull(knowledge3.get())
    }
}