package com.github.klee0kai.test.cache.di;

import com.github.klee0kai.stone.annotations.Module;
import com.github.klee0kai.stone.annotations.Provide;
import com.github.klee0kai.test.data.StoneRepository;


@Module
public interface CacheDataModule {

    @Provide(cache = Provide.CacheType.FACTORY)
    StoneRepository provideFactory();


    @Provide(cache = Provide.CacheType.STRONG)
    StoneRepository provideStrong();

    @Provide(cache = Provide.CacheType.SOFT)
    StoneRepository provideSoft();


    @Provide(cache = Provide.CacheType.WEAK)
    StoneRepository provideWeak();


    StoneRepository provideDefaultSoft();

}
