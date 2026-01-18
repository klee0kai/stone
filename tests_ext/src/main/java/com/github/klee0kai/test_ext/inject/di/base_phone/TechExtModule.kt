package com.github.klee0kai.test_ext.inject.di.base_phone

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test.di.base_phone.TechModule
import com.github.klee0kai.test.di.base_phone.identifiers.RamSize
import com.github.klee0kai.test_ext.inject.tech.components.DDR3Ram

@Module
interface TechExtModule : TechModule {

    @Provide(cache = Provide.CacheType.Weak)
    override fun ram(): DDR3Ram

    @Provide(cache = Provide.CacheType.Weak)
    override fun ram(ramSize: RamSize?): DDR3Ram

}
