package com.github.klee0kai.wiki.init

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test.mowgli.galaxy.Earth

@Module
open class DynamicFeatureModule : FeatureModule() {

    @Provide(cache = Provide.CacheType.Soft)
    override fun earth(): Earth {
        return Earth()
    }

}
