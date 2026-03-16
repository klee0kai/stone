package com.github.klee0kai.test.di.techproviders

import com.github.klee0kai.stone.Named
import com.github.klee0kai.stone.Provider
import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.stone.weakref.Ref
import com.github.klee0kai.stone.weakref.WeakRef
import com.github.klee0kai.stone.wrappers.AsyncCoroutineProvide
import com.github.klee0kai.stone.wrappers.LazyProvide
import com.github.klee0kai.test.di.base_phone.identifiers.DataStorageSize
import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsType
import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsVersion
import com.github.klee0kai.test.di.base_phone.identifiers.RamSize
import com.github.klee0kai.test.tech.components.Battery
import com.github.klee0kai.test.tech.components.DataStorage
import com.github.klee0kai.test.tech.components.OperationSystem
import com.github.klee0kai.test.tech.components.Ram

@Module(genProviderName = "TechFactoryProviders")
interface TechFactoryGenProvideModule {

    @Provide(cache = Provide.CacheType.Factory, provideWrapper = LazyProvide::class)
    fun battery(): Battery?

    @Named("null_args")
    @Provide(cache = Provide.CacheType.Factory)
    fun dataStorage(): DataStorage?

    @Provide(cache = Provide.CacheType.Factory)
    fun dataStorage(size: DataStorageSize?): DataStorage?

    @Named("null_args")
    @Provide(cache = Provide.CacheType.Factory, provideWrapper = Provider::class)
    fun ram(): Ram?

    @Provide(cache = Provide.CacheType.Factory, provideWrapper = Ref::class)
    fun ram(ramSize: RamSize? = null): Ram?


    @Named("null_args")
    @Provide(cache = Provide.CacheType.Factory, provideWrapper = WeakRef::class)
    fun phoneOsNamed(osType: PhoneOsType?): OperationSystem?

    @Provide(cache = Provide.CacheType.Factory, provideWrapper = AsyncCoroutineProvide::class)
    fun phoneOs(
        phoneOsType: PhoneOsType? = PhoneOsType.UbuntuTouch,
        version: PhoneOsVersion? = PhoneOsVersion(version = "def_version")
    ): OperationSystem?
}
