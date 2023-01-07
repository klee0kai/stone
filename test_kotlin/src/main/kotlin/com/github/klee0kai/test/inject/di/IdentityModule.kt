package com.github.klee0kai.test.inject.di

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test.inject.identity.Conscience
import com.github.klee0kai.test.inject.identity.Ideology
import com.github.klee0kai.test.inject.identity.Knowledge

@Module
interface IdentityModule {
    @Provide(cache = Provide.CacheType.Factory)
    fun knowledge(): Knowledge

    @Provide(cache = Provide.CacheType.Factory)
    fun conscience(): Conscience

    @Provide(cache = Provide.CacheType.Soft)
    fun ideology(): Ideology
}