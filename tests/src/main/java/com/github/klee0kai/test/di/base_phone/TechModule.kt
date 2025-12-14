package com.github.klee0kai.test.di.base_phone

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test.di.base_phone.identifiers.DataStorageSize
import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsType
import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsVersion
import com.github.klee0kai.test.di.base_phone.identifiers.RamSize
import com.github.klee0kai.test.tech.components.Battery
import com.github.klee0kai.test.tech.components.DataStorage
import com.github.klee0kai.test.tech.components.OperationSystem
import com.github.klee0kai.test.tech.components.Ram
import javax.inject.Named

@Module
interface TechModule {
    @Provide(cache = Provide.CacheType.Weak)
    fun battery(): Battery?

    @Named("null_args")
    @Provide(cache = Provide.CacheType.Weak)
    fun dataStorage(): DataStorage?

    @Provide(cache = Provide.CacheType.Weak)
    fun dataStorage(size: DataStorageSize?): DataStorage?

    @Provide(cache = Provide.CacheType.Weak)
    fun ram(ramSize: RamSize?): Ram?

    @Named("null_args")
    @Provide(cache = Provide.CacheType.Weak)
    fun ram(): Ram?

    @Named("null_args")
    @Provide(cache = Provide.CacheType.Weak)
    fun phoneOs(osType: PhoneOsType?): OperationSystem?

    @Provide(cache = Provide.CacheType.Weak)
    fun phoneOs(osType: PhoneOsType?, version: PhoneOsVersion?): OperationSystem?
}
