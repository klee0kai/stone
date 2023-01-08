package com.github.klee0kai.test_kotlin.di.base_comp

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test_kotlin.di.base_comp.qualifiers.Company
import com.github.klee0kai.test_kotlin.di.base_comp.qualifiers.KConnectType
import com.github.klee0kai.test_kotlin.di.base_comp.qualifiers.MonitorSize
import com.github.klee0kai.test_kotlin.tech.components.Keyboard
import com.github.klee0kai.test_kotlin.tech.components.Monitor

@Module
interface TechModule {

    @Provide(cache = Provide.CacheType.Weak)
    fun monitor(size: MonitorSize): Monitor

    @Provide(cache = Provide.CacheType.Weak)
    fun monitor(size: MonitorSize, company: Company): Monitor

    @Provide(cache = Provide.CacheType.Weak)
    fun keyboard(connectType: KConnectType = KConnectType.UsbWire): Keyboard


}