package org.example.di;

import com.github.klee0kai.stone.annotations.BindInstance;
import com.github.klee0kai.stone.annotations.Module;
import com.github.klee0kai.stone.annotations.Provide;
import org.example.SimpleApp;

@Module
public interface AppModule {

    @BindInstance(cache = BindInstance.CacheType.WEAK)
    SimpleApp context();

}
