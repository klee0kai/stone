package com.github.klee0kai.test.di.earthmirror

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test.mowgli.earth.Cave
import com.github.klee0kai.test.mowgli.earth.Cave.CaveType

@Module
interface EastModule {
    @Provide(cache = Provide.CacheType.Soft)
    fun cave(): Cave?

    @Provide(cache = Provide.CacheType.Soft)
    fun cave(type: CaveType?, deep: Int?): Cave?
}
