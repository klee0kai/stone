package com.github.klee0kai.test_kotlin.tech.comp

import com.github.klee0kai.test.tech.phone.base.ATech
import com.github.klee0kai.test.tech.phone.base.LifecycleUtils
import com.github.klee0kai.test_kotlin.di.base_comp.qualifiers.KConnectType
import com.github.klee0kai.test_kotlin.di.base_comp.qualifiers.MonitorSize
import com.github.klee0kai.test_kotlin.tech.ComputerStore
import com.github.klee0kai.test_kotlin.tech.components.Keyboard
import com.github.klee0kai.test_kotlin.tech.components.Monitor
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class GameComp(
    val monitorSize: MonitorSize = MonitorSize("17"),
    val kConnectType: KConnectType = KConnectType.Bluetooth,
) : ATech() {

    var lifeCycleOwner = LifecycleUtils.createFromATech(this)

    val uuid = UUID.randomUUID()

    @Inject
    @Named("null_company")
    var monitor: Monitor? = null

    @Inject
    var keyboard: Keyboard? = null

    fun buy() {
        onBuy()
        ComputerStore.DI.inject(this, lifeCycleOwner, monitorSize, kConnectType)
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
        ComputerStore.DI.inject(lifeCycleOwner, this, monitorSize, kConnectType)
    }


}