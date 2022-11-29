package com.github.klee0kai.test.cache.di;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test.data.StoneRepository;


@Module
public interface CacheDataModule {

    @Provide(cache = Provide.CacheType.Factory)
    StoneRepository provideFactory();


    @Provide(cache = Provide.CacheType.Strong)
    StoneRepository provideStrong();

    @Provide(cache = Provide.CacheType.Soft)
    StoneRepository provideSoft();


    @Provide(cache = Provide.CacheType.Weak)
    StoneRepository provideWeak();


    StoneRepository provideDefaultSoft();

}
