package com.github.klee0kai.thekey.stone.ksp.ksp

import com.github.klee0kai.stone.annotations.component.ProtectInjected
import com.github.klee0kai.stone.annotations.component.SwitchCache
import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide

/**
 * fix crash at :tests:kspKotlinWasmJs
 */
val Provide.cacheProtected get() = runCatching { cache }.getOrNull() ?: Provide.CacheType.Factory

/**
 * fix crash at :tests:kspKotlinWasmJs
 */
val Provide.provideWrapperProtected get() = runCatching { provideWrapper }.getOrNull() ?: Nothing::class

/**
 * fix crash at :tests:kspKotlinWasmJs
 */
val BindInstance.cacheProtected get() = runCatching { cache }.getOrNull() ?: BindInstance.CacheType.Soft


/**
 * fix crash at :tests:kspKotlinWasmJs
 */
val SwitchCache.cacheProtected get() = runCatching { cache }.getOrNull() ?: SwitchCache.CacheType.Default

/**
 * fix crash at :tests:kspKotlinWasmJs
 */
val SwitchCache.timeMillisProtected get() = runCatching { timeMillis }.getOrNull() ?: -1


/**
 * fix crash at :tests:kspKotlinWasmJs
 */
val ProtectInjected.timeMillisProtected get() = runCatching { timeMillis }.getOrNull() ?: 5000L

/**
 * fix crash at :tests:kspKotlinWasmJs
 */
val Module.genProviderNameProtected get() = runCatching { genProviderName }.getOrNull()
