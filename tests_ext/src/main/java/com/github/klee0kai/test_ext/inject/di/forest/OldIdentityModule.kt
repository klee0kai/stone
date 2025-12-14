package com.github.klee0kai.test_ext.inject.di.forest

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test.di.base_forest.IdentityModule
import com.github.klee0kai.test_ext.inject.mowgli.identity.FamilyIdeology
import com.github.klee0kai.test_ext.inject.mowgli.identity.OldKnowledge


@Module
interface OldIdentityModule : IdentityModule {

    @Provide(cache = Provide.CacheType.Factory)
    override fun knowledge(): OldKnowledge

    @Provide(cache = Provide.CacheType.Soft)
    override fun ideology(): FamilyIdeology

}
