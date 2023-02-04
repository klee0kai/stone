package com.github.klee0kai.test.di.bindinstance.singlemethod;


import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.test.di.gcforest.scopes.GcSunScope;
import com.github.klee0kai.test.mowgli.galaxy.IStar;
import com.github.klee0kai.test.mowgli.galaxy.Sun;

@Module
public interface SunModule {

    @GcSunScope
    @BindInstance(cache = BindInstance.CacheType.Strong)
    Sun sunStrong(Sun sun);

    @GcSunScope
    @BindInstance(cache = BindInstance.CacheType.Soft)
    Sun sunSoft(Sun sun);


    @GcSunScope
    @BindInstance
    Sun sun(Sun sun);


    @GcSunScope
    @BindInstance(cache = BindInstance.CacheType.Weak)
    IStar star(IStar star);


}
