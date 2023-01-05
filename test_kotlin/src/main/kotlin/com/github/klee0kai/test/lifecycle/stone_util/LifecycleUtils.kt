package com.github.klee0kai.test.lifecycle.stone_util

import com.github.klee0kai.stone.types.lifecycle.IStoneLifeCycleListener
import com.github.klee0kai.stone.types.lifecycle.IStoneLifeCycleOwner
import com.github.klee0kai.test.lifecycle.base.APhone
import com.github.klee0kai.test.lifecycle.base.APhoneLifecycle

object LifecycleUtils {
    fun createFromPhone(phone: APhone): IStoneLifeCycleOwner {
        return IStoneLifeCycleOwner { listener: IStoneLifeCycleListener ->
            phone.subscribe(
                object : APhoneLifecycle {
                    override fun onBuy() {}
                    override fun onBroken() {}
                    override fun onDrown() {
                        listener.protectForInjected(100)
                    }
                }
            )
        }
    }
}