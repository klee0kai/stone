package com.github.klee0kai.stone.__hidden__

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

object StoneScope {
    val stoneCoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
}