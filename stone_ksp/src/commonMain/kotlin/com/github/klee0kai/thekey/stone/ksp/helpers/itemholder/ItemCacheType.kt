package com.github.klee0kai.thekey.stone.ksp.helpers.itemholder

import com.github.klee0kai.stone.__hidden__.types.holders.StoneRefType
import com.github.klee0kai.stone.annotations.component.GcSoftScope
import com.github.klee0kai.stone.annotations.component.GcStrongScope
import com.github.klee0kai.stone.annotations.component.GcWeakScope
import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.stone.annotations.module.Provide
import com.squareup.kotlinpoet.asClassName

enum class ItemCacheType {
    Strong, Soft, Weak;

    val gcScopeClassName
        get() = when (this) {
            Weak -> GcWeakScope::class
            Strong -> GcStrongScope::class
            Soft -> GcSoftScope::class
        }.asClassName()
}

fun ItemCacheType.toRefTypeSingle(

): StoneRefType = when (this) {
    ItemCacheType.Strong -> StoneRefType.StrongObject
    ItemCacheType.Soft -> StoneRefType.SoftObject
    ItemCacheType.Weak -> StoneRefType.WeakObject
}

fun ItemCacheType.toRefTypeList(

): StoneRefType = when (this) {
    ItemCacheType.Strong -> StoneRefType.ListObject
    ItemCacheType.Soft -> StoneRefType.ListSoftObject
    ItemCacheType.Weak -> StoneRefType.ListWeakObject
}


fun BindInstance.CacheType.toItemCacheType(
): ItemCacheType = when (this) {
    BindInstance.CacheType.Weak -> ItemCacheType.Weak
    BindInstance.CacheType.Soft -> ItemCacheType.Soft
    BindInstance.CacheType.Strong -> ItemCacheType.Strong
}

fun Provide.CacheType.toItemCacheType(
): ItemCacheType? = when (this) {
    Provide.CacheType.Factory -> null
    Provide.CacheType.Weak -> ItemCacheType.Weak
    Provide.CacheType.Soft -> ItemCacheType.Soft
    Provide.CacheType.Strong -> ItemCacheType.Strong
}

