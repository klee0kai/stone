package com.github.klee0kai.stone.__hidden__.types

import com.github.klee0kai.stone.annotations.component.SwitchCache
import kotlin.time.Duration

/**
 * Stone Private class
 */
class SwitchCacheParam(
    val cache: SwitchCache.CacheType,
    val time: Duration,
) {
    companion object {
        fun toWeak() = SwitchCacheParam(SwitchCache.CacheType.Weak, Duration.INFINITE)
        fun toDef() = SwitchCacheParam(SwitchCache.CacheType.Default, Duration.INFINITE)
    }
}
