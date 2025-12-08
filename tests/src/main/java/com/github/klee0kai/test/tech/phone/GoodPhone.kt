package com.github.klee0kai.test.tech.phone

import com.github.klee0kai.stone.lifecycle.StoneLifeCycleOwner
import com.github.klee0kai.test.tech.components.Battery
import com.github.klee0kai.test.tech.components.DataStorage
import com.github.klee0kai.test.tech.components.OperationSystem
import com.github.klee0kai.test.tech.components.Ram
import com.github.klee0kai.test.tech.phone.base.ATech
import com.github.klee0kai.test.tech.phone.base.LifecycleUtils
import javax.inject.Inject

class GoodPhone : ATech() {
    @JvmField
    var lifeCycleOwner: StoneLifeCycleOwner = LifecycleUtils.createFromATech(this)


    @JvmField
    @Inject
    var battery: Battery? = null

    @JvmField
    @Inject
    var dataStorage: DataStorage? = null


    @JvmField
    @Inject
    var ram: Ram? = null

    @JvmField
    @Inject
    var os: OperationSystem? = null

    fun dropToWater() {
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
