package com.github.klee0kai.test.gc

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.stone.closed.types.single.SoftItemHolder
import com.github.klee0kai.test.Application
import com.github.klee0kai.test.Context
import com.github.klee0kai.test.gc.di.GComponent
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class GcTests {


    @Test
    fun simpleGcTest() {
        var app: Application? = Application.create()
        var context: Context? = Context.create()
        val DI = Stone.createComponent(GComponent::class.java)
        DI.bindSingle(app)
        DI.bindSingle(context)

        //using items not collect by gc
        DI.gcAll()
        assertNotNull(DI.app().app())
        assertNotNull(DI.app().context())


        //not using items collect by gc
        app = null
        context = null
        DI.gcAll()
        assertNull(DI.app().app())
        assertNull(DI.app().context())
    }

    @Test
    fun appGcScopeTest() {
        var app: Application? = Application.create()
        var context: Context? = Context.create()
        val DI = Stone.createComponent(GComponent::class.java)
        DI.bindSingle(app)
        DI.bindSingle(context)

        //using items not collect by gc
        DI.gcApp()
        assertNotNull(DI.app().app())
        assertNotNull(DI.app().context())

        //not using items collect by gc
        app = null
        context = null
        DI.gcApp()
        assertNull(DI.app().app())
        assertNotNull(DI.app().context())
    }

    @Test
    fun contextGcScopeTest() {
        var app: Application? = Application.create()
        var context: Context? = Context.create()
        val DI = Stone.createComponent(GComponent::class.java)
        DI.bindSingle(app)
        DI.bindSingle(context)

        //using items not collect by gc
        DI.gcContext()
        assertNotNull(DI.app().app())
        assertNotNull(DI.app().context())

        //not using items collect by gc
        app = null
        context = null
        DI.gcContext()
        assertNotNull(DI.app().app())
        assertNull(DI.app().context())
    }

    @Test
    fun multiGcScopeTest() {
        var app: Application? = Application.create()
        var context: Context? = Context.create()
        val DI = Stone.createComponent(GComponent::class.java)
        DI.bindSingle(app)
        DI.bindSingle(context)

        //using items not collect by gc
        DI.gcAppAndContext()
        assertNotNull(DI.app().app())
        assertNotNull(DI.app().context())

        //not using items collect by gc
        app = null
        context = null
        DI.gcAppAndContext()
        assertNull(DI.app().app())
        assertNull(DI.app().context())
    }
}


/**
 * simple kotlin GC bug go-round
 *
 * [Research](https://github.com/klee0kai/KotlinMemLeakResearch)
 */
fun GComponent.bindSingle(obj: Any?) {
    bind(obj)
}