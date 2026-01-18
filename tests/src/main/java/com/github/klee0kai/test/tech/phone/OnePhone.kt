package com.github.klee0kai.test.tech.phone

import com.github.klee0kai.stone.lifecycle.StoneLifeCycleListener
import com.github.klee0kai.stone.lifecycle.StoneLifeCycleOwner
import com.github.klee0kai.test.tech.components.Battery
import com.github.klee0kai.test.tech.components.DataStorage
import com.github.klee0kai.test.tech.components.Ram
import com.github.klee0kai.test.tech.phone.base.ATech
import com.github.klee0kai.test.tech.phone.base.ATechLifecycle
import javax.inject.Inject
import javax.inject.Named

class OnePhone : ATech(), StoneLifeCycleOwner {
    @JvmField
    @Inject
    var battery: Battery? = null

    @JvmField
    @Inject
    @Named("null_args")
    var dataStorage: DataStorage? = null

    @JvmField
    @Inject
    @Named("null_args")
    var ram: Ram? = null


    override fun subscribe(listener: StoneLifeCycleListener) {
        super.subscribe(object : ATechLifecycle {
            override fun onBuy() {
            }

            override fun onBroken() {
            }

            override fun onDrown() {
                listener.protectForInjected(100)
            }
        })
    }


    fun dropToWatter() {
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
}
