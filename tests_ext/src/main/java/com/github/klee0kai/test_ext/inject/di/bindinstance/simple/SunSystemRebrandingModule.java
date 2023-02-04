package com.github.klee0kai.test_ext.inject.di.bindinstance.simple;

import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.test.di.bindinstance.simple.SunSystemModule;
import com.github.klee0kai.test_ext.inject.mowgli.galaxy.Pluton;

@Module
public interface SunSystemRebrandingModule extends SunSystemModule {

    @BindInstance
    Pluton pluton();

}
