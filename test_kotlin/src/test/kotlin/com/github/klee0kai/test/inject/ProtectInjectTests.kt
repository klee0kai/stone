package com.github.klee0kai.test.inject

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.lang.ref.WeakReference

class ProtectInjectTests {
    @Test
    fun withoutProtectInjectTest() {
        // create common component for all app
        val forest = Forest()
        forest.create()

        //use sub app components
        var horse: Horse? = Horse()
        horse!!.born()
        val historyWeakReference = WeakReference(horse.history)

        //check inject is completed
        assertNotNull(horse.blood)
        assertNotNull(horse.knowledge)
        assertNotNull(horse.conscience)
        assertNotNull(horse.history)

        //without protect all not uses should be garbage collected
        horse = null
        Forest.DI!!.gcAll()
        assertNull(historyWeakReference.get())
    }

    @Test
    @Throws(InterruptedException::class)
    fun withProtectInjectTest() {
        // create common component for all app
        val forest = Forest()
        forest.create()

        //use sub app components
        var horse: Horse? = Horse()
        horse!!.born()
        val historyWeakReference = WeakReference(horse.history)

        //check inject is completed
        assertNotNull(horse.blood)
        assertNotNull(horse.knowledge)
        assertNotNull(horse.conscience)
        assertNotNull(horse.history)

        //without protect all not uses should be garbage collected
        Forest.DI!!.protectInjected(horse)
        horse = null
        Forest.DI!!.gcAll()
        assertNotNull(historyWeakReference.get())

        //after protect finished
        Thread.sleep(60)
        Forest.DI!!.gcAll()
        assertNull(historyWeakReference.get())
    }

    @Test
    fun ignoreWrappersTest() {
        // create common component for all app
        val forest = Forest()
        forest.create()

        //use sub app components
        val school = School()
        school.build()
        val history = WeakReference(school.historyLazyProvide!!.get())
        val knowledge1 = WeakReference(school.knowledgePhantomProvide!!.get())
        val knowledge2 = WeakReference(school.knowledgePhantomProvide2!!.get())
        val knowledge3 = WeakReference(school.knowledgePhantomProvide3!!.get())


        Forest.DI!!.protectInjected(school)
        Forest.DI!!.gcAll()


        assertNotNull(history.get())
        assertNull(knowledge1.get())
        assertNull(knowledge2.get())
        assertNull(knowledge3.get())
    }
}