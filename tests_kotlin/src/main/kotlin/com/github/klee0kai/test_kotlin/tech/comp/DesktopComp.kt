package com.github.klee0kai.test_kotlin.tech.comp

import com.github.klee0kai.stone.lifecycle.StoneLifeCycleListener
import com.github.klee0kai.stone.lifecycle.StoneLifeCycleOwner
import com.github.klee0kai.test.tech.phone.base.ATech
import com.github.klee0kai.test.tech.phone.base.ATechLifecycle
import com.github.klee0kai.test_kotlin.di.base_comp.identifiers.Company
import com.github.klee0kai.test_kotlin.di.base_comp.identifiers.KConnectType
import com.github.klee0kai.test_kotlin.di.base_comp.identifiers.MonitorSize
import com.github.klee0kai.test_kotlin.tech.ComputerStore
import com.github.klee0kai.test_kotlin.tech.components.Keyboard
import com.github.klee0kai.test_kotlin.tech.components.Monitor
import java.util.*
import javax.inject.Inject

class DesktopComp(
    val monitorSize: MonitorSize = MonitorSize("17"),
    val monCompany: Company = Company("lg"),
    val kConnectType: KConnectType = KConnectType.Din6Connector,
) : ATech(), StoneLifeCycleOwner {

    val uuid = UUID.randomUUID()

    @Inject
    var monitor: Monitor? = null

    @Inject
    var keyboard: Keyboard? = null

    fun buy() {
        onBuy()
        ComputerStore.DI.inject(this, monitorSize, monCompany, kConnectType)
    }

    override fun subscribe(listener: StoneLifeCycleListener?) {
        super.subscribe(object : ATechLifecycle {
            override fun onBuy() = Unit
            override fun onBroken() = Unit

            override fun onDrown() {
                listener?.protectForInjected(100)
            }
        })
    }

    fun dropToWater() {
        onDrown()
        monitor = null
        keyboard = null
    }

    fun broke() {
        onBroken()
        monitor = null
        keyboard = null
    }

    fun repair() {
        ComputerStore.DI.inject(this, monitorSize, monCompany, kConnectType)
    }


}