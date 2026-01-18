package com.github.klee0kai.thekey.stone.ksp.coroutines

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update

class LaunchConductor {

    private val parallelWorkCounter = MutableStateFlow(0)

    suspend fun <T> finishTogether(
        suspendFun: suspend () -> T,
    ): T {
        parallelWorkCounter.update { it + 1 }
        try {
            return suspendFun()
        } finally {
            parallelWorkCounter.update { it - 1 }
            parallelWorkCounter.first { it <= 0 }
        }
    }

}