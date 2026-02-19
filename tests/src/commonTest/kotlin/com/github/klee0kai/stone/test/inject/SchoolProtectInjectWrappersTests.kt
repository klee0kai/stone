package com.github.klee0kai.stone.test.inject

import com.github.klee0kai.stone.weakref.WeakRef
import com.github.klee0kai.test.di.base_forest.ForestComponentStoneComponent
import com.github.klee0kai.test.mowgli.School
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import java.lang.ref.WeakReference
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class SchoolProtectInjectWrappersTests {

    @Test
    fun lazyWrapperProtectTest() {
        // Given
        val DI = ForestComponentStoneComponent()
        val school = School()

        //When
        DI.inject(school)

        val history = WeakRef(school.historyLazyProvide!!.get())
        DI.protectInjected(school)
        DI.gcAll()

        //Then
        assertNotNull(history.get())
    }

    @Test
    fun ignoreProvideWrapperProtectTest() {
        // Given
        val DI = ForestComponentStoneComponent()
        val school: School = School()

        //When
        DI.inject(school)
        val knowledge1 = WeakRef(school.knowledgePhantomProvide!!.get())
        val knowledge2 = WeakRef(school.knowledgePhantomProvide2!!.get())
        val knowledge3 = WeakRef(school.knowledgePhantomProvide3!!.get())
        DI.protectInjected(school)
        DI.gcAll()

        //Then
        assertNull(knowledge1.get())
        assertNull(knowledge2.get())
        assertNull(knowledge3.get())
    }

}
