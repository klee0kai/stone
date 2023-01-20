package com.github.klee0kai.test.di.bindinstance.singlemethod;


import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.test.mowgli.galaxy.*;

import java.lang.ref.WeakReference;

@Module
public interface SunModule {

    @BindInstance
    Sun sun(Sun sun);

    @BindInstance(cache = BindInstance.CacheType.Weak)
    IStar star(IStar star);


}
