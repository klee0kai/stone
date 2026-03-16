package com.github.klee0kai.test.di.base_forest

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test.mowgli.body.Blood

@Module
abstract class UnitedBlueModule : UnitedModule() {

    @Provide(cache = Provide.CacheType.Strong)
    override fun blood(): Blood? {
        return Blood(1)
    }

}
