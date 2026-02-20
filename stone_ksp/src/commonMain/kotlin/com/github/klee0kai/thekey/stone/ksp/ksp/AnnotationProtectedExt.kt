package com.github.klee0kai.thekey.stone.ksp.ksp

import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.stone.annotations.module.Provide

/**
 * fix crash at :tests:kspKotlinWasmJs
 */
val Provide.cacheProtected get() = runCatching { cache }.getOrNull() ?: Provide.CacheType.Factory

/**
 * fix crash at :tests:kspKotlinWasmJs
 */
val BindInstance.cacheProtected get() = runCatching { cache }.getOrNull() ?: BindInstance.CacheType.Soft