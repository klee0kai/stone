package com.github.klee0kai.test.bindinstance

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.Application
import com.github.klee0kai.test.Context
import com.github.klee0kai.test.bindinstance.di.BindInstanceTextComponent
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class BindInstanceTests {
    @Test
    fun simpleInitTest() {
        val application = Application.create()
        val context = Context.create()
        val DI = Stone.createComponent(
            BindInstanceTextComponent::class.java
        )
        DI.bind(application, context)
        Assertions.assertNotEquals(
            DI.appModule().application()!!.uuid, DI.appModule().context()!!.uuid
        )
    }

    @Test
    fun simpleInitTest2() {
        val application = Application.create()
        val context = Context.create()
        val DI = Stone.createComponent(
            BindInstanceTextComponent::class.java
        )
        DI.bind(context, application)
        Assertions.assertNotEquals(
            DI.appModule().application()!!.uuid, DI.appModule().context()!!.uuid
        )
    }

    @Test
    fun simpleInitTest3() {
        val application = Application.create()
        val context = Context.create()
        val DI = Stone.createComponent(
            BindInstanceTextComponent::class.java
        )
        DI.bind(application)
        Assertions.assertNull(DI.appModule().context())
        Assertions.assertNotNull(DI.appModule().application())
    }

    @Test
    fun simpleInitTest4() {
        val application = Application.create()
        val context = Context.create()
        val DI = Stone.createComponent(
            BindInstanceTextComponent::class.java
        )
        DI.bind(context)
        Assertions.assertNotNull(DI.appModule().context())
        Assertions.assertNull(DI.appModule().application())
    }
}