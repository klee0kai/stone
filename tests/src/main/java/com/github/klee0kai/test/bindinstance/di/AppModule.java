package com.github.klee0kai.test.bindinstance.di;

import com.github.klee0kai.stone.annotations.Module;
import com.github.klee0kai.stone.annotations.BindInstance;
import com.github.klee0kai.test.bindinstance.Application;
import com.github.klee0kai.test.bindinstance.Context;

@Module
public interface AppModule {

    @BindInstance
    Application application();


    @BindInstance
    Context context();

}
