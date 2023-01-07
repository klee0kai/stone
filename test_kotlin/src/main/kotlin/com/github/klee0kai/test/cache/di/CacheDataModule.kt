package com.github.klee0kai.test.cache.di

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test.data.StoneRepository
import com.github.klee0kai.test.cache.di.ann.StoneSoftScope
import com.github.klee0kai.test.cache.di.ann.StoneStrongScope

@Module
interface CacheDataModule {
    @Provide(cache = Provide.CacheType.Factory)
    fun provideFactory(): StoneRepository

    @StoneStrongScope
    @Provide(cache = Provide.CacheType.Strong)
    fun provideStrong(): StoneRepository

    @StoneSoftScope
    @Provide(cache = Provide.CacheType.Soft)
    fun provideSoft(): StoneRepository

    @Provide(cache = Provide.CacheType.Weak)
    fun provideWeak(): StoneRepository
    fun provideDefaultSoft(): StoneRepository
}