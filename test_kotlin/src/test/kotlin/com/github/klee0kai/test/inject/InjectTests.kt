package com.github.klee0kai.test.inject

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class InjectTests {
    @Test
    fun simpleInjectTest() {
        // create common component for all app
        val forest = Forest()
        forest.create()

        //use sub app components
        val horse = Horse()
        horse.born()
        val snake = Snake()
        snake.born()
        val mowgli = Mowgli()
        mowgli.born()

        //check inject is completed
        assertNotNull(horse.blood)
        assertNotNull(horse.knowledge)
        assertNotNull(horse.conscience)
        assertNotNull(horse.knowledge)
        assertNotNull(snake.blood)
        assertNotNull(snake.knowledge)
        assertNotNull(snake.conscience)
        assertNotNull(snake.knowledge)
        assertNotNull(mowgli.blood)
        assertNotNull(mowgli.knowledge)
        assertNotNull(mowgli.conscience)
        assertNotNull(mowgli.knowledge)

        //check cached items
        assertEquals(horse.blood!!.uuid, snake.blood.uuid)
        assertEquals(horse.blood!!.uuid, mowgli.blood!!.uuid)
        assertEquals(horse.earth!!.uuid, snake.earth.uuid)
        assertEquals(horse.earth!!.uuid, mowgli.earth!!.uuid)

        //check factory items
        assertNotEquals(horse.conscience.uuid, snake.conscience.uuid)
        assertNotEquals(horse.conscience.uuid, mowgli.conscience!!.uuid)
        assertNotEquals(horse.knowledge.uuid, snake.knowledge.uuid)
        assertNotEquals(horse.knowledge.uuid, mowgli.knowledge!!.uuid)
    }

    @Test
    fun fieldsInjectTest() {
        // create common component for all app
        val forest = Forest()
        forest.create()

        //use sub app components
        val mowgli = Mowgli()
        mowgli.born()
        assertNotNull(mowgli.knowledge)
        assertNotNull(mowgli.knowledgeWeakRef!!.get())
        assertNotNull(mowgli.knowledgeSoftRef!!.get())
        assertNotNull(mowgli.knowledgeLazyProvide!!.get())
        assertNotNull(mowgli.knowledgePhantomProvide!!.get())
        assertNotNull(mowgli.knowledgePhantomProvide2!!.get())
        assertNotNull(mowgli.knowledgePhantomProvide3!!.get())
    }

    @Test
    fun refWrapperTest() {
        // create common component for all app
        val forest = Forest()
        forest.create()

        //use sub app components
        val mowgli = Mowgli()
        mowgli.born()
        assertEquals(
            mowgli.knowledgeWeakRef!!.get()!!.uuid,
            mowgli.knowledgeWeakRef!!.get()!!.uuid
        )
        assertEquals(
            mowgli.knowledgeSoftRef!!.get()!!.uuid,
            mowgli.knowledgeSoftRef!!.get()!!.uuid
        )
        assertEquals(
            mowgli.knowledgeLazyProvide!!.get()!!.uuid,
            mowgli.knowledgeLazyProvide!!.get()!!.uuid
        )
    }

    @Test
    fun genWrapperTest() {
        // create common component for all app
        val forest = Forest()
        forest.create()

        //use sub app components
        val mowgli = Mowgli()
        mowgli.born()
        assertNotEquals(
            mowgli.knowledgePhantomProvide!!.get().uuid,
            mowgli.knowledgePhantomProvide!!.get().uuid
        )
        assertNotEquals(
            mowgli.knowledgePhantomProvide2!!.get().uuid,
            mowgli.knowledgePhantomProvide2!!.get().uuid
        )
        assertNotEquals(
            mowgli.knowledgePhantomProvide3!!.get().uuid,
            mowgli.knowledgePhantomProvide3!!.get().uuid
        )
    }
}