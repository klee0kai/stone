package com.github.klee0kai.test.tech.phone.base

import com.github.klee0kai.stone.lifecycle.StoneLifeCycleListener
import com.github.klee0kai.stone.lifecycle.StoneLifeCycleOwner

object LifecycleUtils {
    fun createFromATech(phone: ATech): StoneLifeCycleOwner {
        return StoneLifeCycleOwner { listener: StoneLifeCycleListener? ->
            phone.subscribe(
                object : ATechLifecycle {
                    override fun onBuy() {
                    }

                    override fun onBroken() {
                    }

                    override fun onDrown() {
                        listener!!.protectForInjected(100)
                    }
                }
            )
        }
    }
}
