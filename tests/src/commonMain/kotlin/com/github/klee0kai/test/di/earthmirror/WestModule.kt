package com.github.klee0kai.test.di.earthmirror

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test.mowgli.earth.*
import com.github.klee0kai.test.mowgli.earth.Cave.CaveType

@Module
abstract class WestModule {
    @Provide(cache = Provide.CacheType.Factory)
    open fun riverImpl(): IRiver? {
        return River()
    }

    @Provide(cache = Provide.CacheType.Soft)
    open fun mountainImp(): IMountain? {
        return Mountain()
    }

    @Provide(cache = Provide.CacheType.Soft)
    abstract fun cave(): Cave?

    @Provide(cache = Provide.CacheType.Soft)
    abstract fun cave(type: CaveType?, deep: Int?): Cave?
}
