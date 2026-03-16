package com.github.klee0kai.test.tech.phone.base

abstract class ATech {

    private val listeners = mutableListOf<ATechLifecycle>()

    fun subscribe(listener: ATechLifecycle?) {
        this.listeners.add(listener!!)
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
