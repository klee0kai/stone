package com.github.klee0kai.stone.__hidden__

import com.github.klee0kai.stone.__hidden__.SwitchCacheParam
import com.github.klee0kai.stone.annotations.component.SwitchCache

/**
 * Stone Private class
 */
class CacheAction(
    val type: ActionType?,
    val swCacheParams: SwitchCacheParam? = null,
    val value: Any? = null,
) {

    enum class ActionType {
        GET_VALUE,
        SET_VALUE,
        SET_IF_NULL,
        SWITCH_CACHE,
    }

    val isGetAction: Boolean get() = type == ActionType.GET_VALUE

    val isSetAction: Boolean get() = type == ActionType.SET_VALUE

    val isSetIfNullAction: Boolean get() = type == ActionType.SET_IF_NULL

    val isSwitchCacheAction: Boolean get() = type == ActionType.SWITCH_CACHE

    companion object {
        val valueAction: CacheAction get() = CacheAction(ActionType.GET_VALUE)

        fun setValueAction(value: Any?) = CacheAction(ActionType.SET_VALUE, value = value)

        fun setIfNullValueAction(value: Any?) = CacheAction(ActionType.SET_IF_NULL, value = value)

        fun switchCacheValueAction(
            param: SwitchCacheParam?,
        ) = CacheAction(
            type = ActionType.SWITCH_CACHE,
            swCacheParams = param,
        )

        fun switchCacheToDefAction(
        ) = CacheAction(
            type = ActionType.SWITCH_CACHE,
            swCacheParams = SwitchCacheParam(SwitchCache.CacheType.Default),
        )
    }
}


