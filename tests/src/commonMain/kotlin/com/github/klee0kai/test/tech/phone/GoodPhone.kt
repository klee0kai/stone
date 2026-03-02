package com.github.klee0kai.test.tech.phone

import com.github.klee0kai.stone.lifecycle.StoneLifeCycleOwner
import com.github.klee0kai.stone.Inject
import com.github.klee0kai.test.tech.components.Battery
import com.github.klee0kai.test.tech.components.DataStorage
import com.github.klee0kai.test.tech.components.OperationSystem
import com.github.klee0kai.test.tech.components.Ram
import com.github.klee0kai.test.tech.phone.base.ATech
import com.github.klee0kai.test.tech.phone.base.LifecycleUtils

class GoodPhone : ATech() {

    var lifeCycleOwner: StoneLifeCycleOwner = LifecycleUtils.createFromATech(this)


    @Inject
    var battery: Battery? = null

    @Inject
    var dataStorage: DataStorage? = null

    @Inject
    var ram: Ram? = null

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
