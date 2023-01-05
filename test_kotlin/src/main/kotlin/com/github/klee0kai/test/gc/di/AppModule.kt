package com.github.klee0kai.test.gc.di

import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.test.Application
import com.github.klee0kai.test.Context
import com.github.klee0kai.test.gc.di.ann.AppGcScope
import com.github.klee0kai.test.gc.di.ann.ContextGcScope

@Module
interface AppModule {
    @AppGcScope
    @BindInstance
    fun app(): Application?

    @ContextGcScope
    @BindInstance
    fun context(): Context?
}