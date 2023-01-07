package com.github.klee0kai.test.gc

import com.github.klee0kai.stone.annotations.component.GcAllScope
import com.github.klee0kai.stone.annotations.component.SwitchCache
import com.github.klee0kai.stone.closed.types.single.SoftItemHolder
import com.github.klee0kai.test.Application
import com.github.klee0kai.test.Context
import com.github.klee0kai.test.gc.di.AppModule
import com.github.klee0kai.test.gc.di.AppModuleStoneModule
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.lang.ref.Reference
import java.lang.ref.SoftReference
import java.lang.ref.WeakReference


class TempGsTests {


    @Test
    fun moduleTest() {
        var i = 10
        val appRef = SoftItemHolder<Application>()
        appRef.set(Application.create())
        val appModule = AppModuleStoneModule()
        appModule.bind(Application.create())

        appModule.switchRef(setOf(GcAllScope::class.java), SwitchCache.CacheType.Weak, null, -1)
        assertNotNull(appModule.app())

        System.gc()

        appModule.switchRef(setOf(GcAllScope::class.java), SwitchCache.CacheType.Default, null, -1)


        assertNull(appModule.app())
    }

    fun bind(appModule: AppModuleStoneModule, app: Application?) {
        for (o in listOf(app)) {
            appModule.bind(o)
        }
    }

    @Test
    fun moduleTest2() {
        var app: Application? = Application.create()
        val appModule = AppModuleStoneModule()
        bind(appModule, app)
        app = null

        appModule.switchRef(setOf(GcAllScope::class.java), SwitchCache.CacheType.Weak, null, -1)
        assertNotNull(appModule.app())

        System.gc()

        assertNull(appModule.app())
    }

    @Test
    fun preGcTest() {

        var appRef = SoftItemHolder<Application>()
        appRef.set(Application.create())
        appRef.weak()

        System.gc()
        appRef.defRef()

        assertNull(appRef.get())
    }


    @Test
    fun test2() {
        var appSoft: Reference<Application>? = SoftReference(Application.create())
        var appWeak: Reference<Application>? = WeakReference(appSoft!!.get())

        assertNotNull(appWeak!!.get())
        appSoft = null
        System.gc()
        assertNull(appWeak.get())

    }
}