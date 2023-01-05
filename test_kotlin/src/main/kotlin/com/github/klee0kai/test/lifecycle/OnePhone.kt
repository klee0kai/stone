package com.github.klee0kai.test.lifecycle

import com.github.klee0kai.stone.types.lifecycle.IStoneLifeCycleListener
import com.github.klee0kai.stone.types.lifecycle.IStoneLifeCycleOwner
import com.github.klee0kai.test.lifecycle.base.APhone
import com.github.klee0kai.test.lifecycle.base.APhoneLifecycle
import com.github.klee0kai.test.lifecycle.structure.Battery
import com.github.klee0kai.test.lifecycle.structure.DataStorage
import com.github.klee0kai.test.lifecycle.structure.Ram
import javax.inject.Inject

class OnePhone : APhone(), IStoneLifeCycleOwner {
    @Inject
    var battery: Battery? = null

    @Inject
    var dataStorage: DataStorage? = null

    @Inject
    var ram: Ram? = null
    override fun subscribe(listener: IStoneLifeCycleListener) {
        super.subscribe(object : APhoneLifecycle {
            override fun onBuy() {}
            override fun onBroken() {}
            override fun onDrown() {
                listener.protectForInjected(100)
            }
        })
    }

    fun buy() {
        onBuy()
        PhoneStore.DI.inject(this)
    }

    fun drown() {
        onDrown()
        battery = null
        dataStorage = null
        ram = null
    }

    fun broke() {
        onBroken()
        battery = null
        dataStorage = null
        ram = null
    }

    fun repair() {
        PhoneStore.DI.inject(this)
    }
}