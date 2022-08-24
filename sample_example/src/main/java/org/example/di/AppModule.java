package org.example.di;

import com.github.klee0kai.stone.annotations.ChangeableSingleton;
import com.github.klee0kai.stone.annotations.Module;
import com.github.klee0kai.stone.annotations.Singleton;
import org.example.SimpleApp;

@Module
public interface AppModule {

    @ChangeableSingleton(cache = Singleton.CacheType.WEAK)
    SimpleApp context();

}
