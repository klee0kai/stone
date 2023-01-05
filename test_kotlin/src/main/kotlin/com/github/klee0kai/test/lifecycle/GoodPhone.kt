package com.github.klee0kai.test.lifecycle

import com.github.klee0kai.stone.types.lifecycle.IStoneLifeCycleOwner
import com.github.klee0kai.test.lifecycle.base.APhone
import com.github.klee0kai.test.lifecycle.di.qualifier.DataStorageSize
import com.github.klee0kai.test.lifecycle.di.qualifier.RamSize
import com.github.klee0kai.test.lifecycle.stone_util.LifecycleUtils
import com.github.klee0kai.test.lifecycle.structure.Battery
import com.github.klee0kai.test.lifecycle.structure.DataStorage
import com.github.klee0kai.test.lifecycle.structure.Ram
import javax.inject.Inject

class GoodPhone(dataStorageSize: DataStorageSize, ramSize: RamSize) : APhone() {
    var lifeCycleOwner: IStoneLifeCycleOwner = LifecycleUtils.createFromPhone(this)
    private val dataStorageSize: DataStorageSize
    private val ramSize: RamSize

    @Inject
    var battery: Battery? = null

    @Inject
    var dataStorage: DataStorage? = null

    @Inject
    var ram: Ram? = null

    init {
        this.dataStorageSize = dataStorageSize
        this.ramSize = ramSize
    }

    fun buy() {
        onBuy()
        PhoneStore.DI.inject(this, lifeCycleOwner, dataStorageSize, ramSize)
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
        PhoneStore.DI.inject(this, dataStorageSize, ramSize)
    }
}