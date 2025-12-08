package com.github.klee0kai.test.di.base_forest

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test.mowgli.identity.Conscience
import com.github.klee0kai.test.mowgli.identity.Ideology
import com.github.klee0kai.test.mowgli.identity.Knowledge


@Module
interface IdentityModule {
    @Provide(cache = Provide.CacheType.Factory)
    fun knowledge(): Knowledge?

    @Provide(cache = Provide.CacheType.Factory)
    fun conscience(): Conscience?

    @Provide(cache = Provide.CacheType.Soft)
    fun ideology(): Ideology?
}
