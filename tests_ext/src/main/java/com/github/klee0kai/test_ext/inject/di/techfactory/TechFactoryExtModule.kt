package com.github.klee0kai.test_ext.inject.di.techfactory

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test.di.base_phone.identifiers.RamSize
import com.github.klee0kai.test.di.techfactory.TechFactoryModule
import com.github.klee0kai.test_ext.inject.di.techfactory.identifiers.Frequency
import com.github.klee0kai.test_ext.inject.tech.components.DDR3Ram
import javax.inject.Named

@Module
interface TechFactoryExtModule : TechFactoryModule {

    @Named
    @Provide(cache = Provide.CacheType.Factory)
    override fun ram(): DDR3Ram?

    @Named("size")
    @Provide(cache = Provide.CacheType.Factory)
    override fun ram(ramSize: RamSize?): DDR3Ram?

    @Named("size-frequency")
    @Provide(cache = Provide.CacheType.Factory)
    fun ram(ramSize: RamSize?, frequency: Frequency?): DDR3Ram

}
