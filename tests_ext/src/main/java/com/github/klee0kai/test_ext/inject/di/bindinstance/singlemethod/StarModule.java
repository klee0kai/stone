package com.github.klee0kai.test_ext.inject.di.bindinstance.singlemethod;

import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.test.di.bindinstance.singlemethod.SunModule;
import com.github.klee0kai.test_ext.inject.di.gcscopes.GcSiriusScope;
import com.github.klee0kai.test_ext.inject.mowgli.galaxy.stars.Sirius;

@Module
public interface StarModule extends SunModule {

    @GcSiriusScope
    @BindInstance(cache = BindInstance.CacheType.Strong)
    Sirius siriusStrong(Sirius sirius);

    @GcSiriusScope
    @BindInstance(cache = BindInstance.CacheType.Soft)
    Sirius siriusSoft(Sirius sirius);


    @GcSiriusScope
    @BindInstance
    Sirius sirius(Sirius sirius);


    @GcSiriusScope
    @BindInstance(cache = BindInstance.CacheType.Weak)
    Sirius siriusWeak(Sirius star);


}
