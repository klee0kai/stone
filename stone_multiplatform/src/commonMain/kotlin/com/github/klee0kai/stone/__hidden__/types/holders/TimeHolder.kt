package com.github.klee0kai.stone.__hidden__.types.holders

import com.github.klee0kai.stone.weakref.Ref
import com.github.klee0kai.stone.weakref.SoftRef
import kotlinx.coroutines.*

/**
 * Stone Private class
 */
class TimeHolder<T>(
    private var scope: CoroutineScope = CoroutineScope(SupervisorJob()),
    ob: T? = null,
    holdTime: Long? = null,
) : Ref<T?> {

    var ref: Ref<T?>? = null

    init {
        if (ob != null && holdTime != null) {
            hold(ob, holdTime)
        }
    }

    private var holdJob: Job? = null

    fun hold(ob: T?, holdTime: Long): T? {
        clearRef()
        this.ref = SoftRef(ob)
        holdJob?.cancel()
        holdJob = scope.launch {
            delay(holdTime)
            clearRef()
        }
        return ob
    }

    override fun get(): T? = ref?.get()

    private fun clearRef() {
        ref = null
        holdJob?.cancel()
        holdJob = null
    }

}
