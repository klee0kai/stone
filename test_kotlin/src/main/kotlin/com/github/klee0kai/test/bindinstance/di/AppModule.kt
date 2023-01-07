package com.github.klee0kai.test.bindinstance.di

import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.test.Application
import com.github.klee0kai.test.Context

@Module
interface AppModule {
    @BindInstance
    fun application(): Application?

    @BindInstance
    fun context(): Context?
}