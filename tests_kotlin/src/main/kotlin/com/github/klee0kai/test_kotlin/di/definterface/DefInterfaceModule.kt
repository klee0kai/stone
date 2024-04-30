package com.github.klee0kai.test_kotlin.di.definterface

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test_kotlin.di.base_comp.identifiers.Company
import com.github.klee0kai.test_kotlin.di.base_comp.identifiers.KConnectType
import com.github.klee0kai.test_kotlin.di.base_comp.identifiers.MonitorSize
import com.github.klee0kai.test_kotlin.tech.components.Keyboard
import com.github.klee0kai.test_kotlin.tech.components.Monitor
import com.github.klee0kai.test_kotlin.tech.components.Mouse

@Module
interface DefInterfaceModule {

    @Provide(cache = Provide.CacheType.Factory)
    fun monitor(size: MonitorSize): Monitor = Monitor(size, Company("samsung"))

    @Provide(cache = Provide.CacheType.Factory)
    fun keyboard(): Keyboard = Keyboard(KConnectType.Bluetooth)

    @Provide(cache = Provide.CacheType.Factory)
    fun mouse(): Mouse

}