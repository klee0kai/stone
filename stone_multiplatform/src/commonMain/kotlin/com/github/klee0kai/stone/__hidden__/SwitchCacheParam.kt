package com.github.klee0kai.stone.__hidden__

import com.github.klee0kai.stone.annotations.component.SwitchCache
import kotlin.time.Duration

/**
 * Stone Private class
 */
class SwitchCacheParam(
    val cache: SwitchCache.CacheType,
    val time: Duration = Duration.INFINITE,
) {
    companion object {
        fun toWeak() = SwitchCacheParam(SwitchCache.CacheType.Weak)
        fun toDef() = SwitchCacheParam(SwitchCache.CacheType.Default)
    }
}