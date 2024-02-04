package com.github.klee0kai.wiki.cachecontrol

import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.test.mowgli.galaxy.Mercury

@Module
interface PlanetsModule {

    @GcMercuryScope
    @BindInstance
    fun mercury(sirius: Mercury? = null): Mercury?

}
