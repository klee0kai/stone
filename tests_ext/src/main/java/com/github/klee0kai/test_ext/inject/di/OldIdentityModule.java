package com.github.klee0kai.test_ext.inject.di;


import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test.di.base_forest.IdentityModule;
import com.github.klee0kai.test_ext.inject.mowgli.identity.FamilyIdeology;
import com.github.klee0kai.test_ext.inject.mowgli.identity.OldKnowledge;


@Module
public interface OldIdentityModule extends IdentityModule {

    @Override
    @Provide(cache = Provide.CacheType.Factory)
    OldKnowledge knowledge();

    @Override
    @Provide(cache = Provide.CacheType.Soft)
    FamilyIdeology ideology();
}
