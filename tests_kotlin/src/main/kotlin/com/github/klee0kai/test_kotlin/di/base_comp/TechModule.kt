package com.github.klee0kai.test_kotlin.di.base_comp

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test_kotlin.di.base_comp.identifiers.Company
import com.github.klee0kai.test_kotlin.di.base_comp.identifiers.KConnectType
import com.github.klee0kai.test_kotlin.di.base_comp.identifiers.MonitorSize
import com.github.klee0kai.test_kotlin.tech.components.Keyboard
import com.github.klee0kai.test_kotlin.tech.components.Monitor
import javax.inject.Named

@Module
interface TechModule {

    @Named("null_company")
    @Provide(cache = Provide.CacheType.Weak)
    fun monitor(size: MonitorSize): Monitor

    @Provide(cache = Provide.CacheType.Weak)
    fun monitor(size: MonitorSize, company: Company): Monitor

    @Provide(cache = Provide.CacheType.Weak)
    fun keyboard(connectType: KConnectType = KConnectType.UsbWire): Keyboard


}