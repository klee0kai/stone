package com.github.klee0kai.test_kotlin.tech.comp

import com.github.klee0kai.test.tech.phone.base.ATechLifecycle

abstract class AComp {
    private val listeners = mutableListOf<ATechLifecycle>()

    fun subscribe(listener: ATechLifecycle) {
        listeners.add(listener)
    }

    fun onBuy() {
        for (lis in listeners) lis.onBuy()
    }

    fun onBroken() {
        for (lis in listeners) lis.onBroken()
    }

    fun onDrown() {
        for (lis in listeners) lis.onDrown()
    }

}