package com.github.klee0kai.test.gc.di

import com.github.klee0kai.stone.annotations.component.*
import com.github.klee0kai.stone.interfaces.IComponent
import com.github.klee0kai.test.cache.di.CacheDataModule
import com.github.klee0kai.test.gc.di.ann.AppGcScope
import com.github.klee0kai.test.gc.di.ann.ContextGcScope

@Component
interface GComponent : IComponent {
    fun app(): AppModule
    fun data(): CacheDataModule

    @GcAllScope
    fun gcAll()

    @GcAllScope
    @SwitchCache(cache = SwitchCache.CacheType.Weak)
    fun allWeak()

    @GcWeakScope
    fun gcWeak()

    @GcSoftScope
    fun gcSoft()

    @GcStrongScope
    fun gcStrong()

    @AppGcScope
    fun gcApp()

    @ContextGcScope
    fun gcContext()

    @AppGcScope
    @ContextGcScope
    fun gcAppAndContext()
}