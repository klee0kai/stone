package com.github.klee0kai.test.di.techfactory

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
interface TechFactoryModule {
    @Provide(cache = Provide.CacheType.Factory)
    fun battery(): Battery?

    @Named("null_args")
    @Provide(cache = Provide.CacheType.Factory)
    fun dataStorage(): DataStorage?

    @Provide(cache = Provide.CacheType.Factory)
    fun dataStorage(size: DataStorageSize?): DataStorage?


    @Named("null_args")
    @Provide(cache = Provide.CacheType.Factory)
    fun ram(): Ram?

    @Provide(cache = Provide.CacheType.Factory)
    fun ram(ramSize: RamSize?): Ram?


    @Named("null_args")
    @Provide(cache = Provide.CacheType.Factory)
    fun phoneOs(osType: PhoneOsType?): OperationSystem?

    @Provide(cache = Provide.CacheType.Factory)
    fun phoneOs(phoneOsType: PhoneOsType?, version: PhoneOsVersion?): OperationSystem?
}
