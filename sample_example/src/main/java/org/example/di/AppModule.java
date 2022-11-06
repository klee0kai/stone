package org.example.di;

import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.stone.annotations.module.Module;
import org.example.SimpleApp;

@Module
public interface AppModule {

    @BindInstance(cache = BindInstance.CacheType.WEAK)
    SimpleApp context();

}
