package com.github.klee0kai.test.lifecycle.di

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test.lifecycle.di.qualifier.DataStorageSize
import com.github.klee0kai.test.lifecycle.di.qualifier.RamSize
import com.github.klee0kai.test.lifecycle.structure.Battery
import com.github.klee0kai.test.lifecycle.structure.DataStorage
import com.github.klee0kai.test.lifecycle.structure.Ram

@Module
interface Structure {
    @Provide(cache = Provide.CacheType.Weak)
    fun battery(): Battery?

    @Provide(cache = Provide.CacheType.Weak)
    fun dataStorage(): DataStorage?

    @Provide(cache = Provide.CacheType.Weak)
    fun dataStorage(size: DataStorageSize?): DataStorage?

    @Provide(cache = Provide.CacheType.Weak)
    fun ram(): Ram?

    @Provide(cache = Provide.CacheType.Weak)
    fun ram(ramSize: RamSize?): Ram?
}