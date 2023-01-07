package com.github.klee0kai.test.lifecycle.base

import java.util.*

abstract class APhone {
    private val listeners = LinkedList<APhoneLifecycle>()
    fun subscribe(listener: APhoneLifecycle) {
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