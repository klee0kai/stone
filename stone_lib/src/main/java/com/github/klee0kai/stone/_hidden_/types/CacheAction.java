package com.github.klee0kai.stone._hidden_.types;

import com.github.klee0kai.stone.annotations.component.SwitchCache;

/**
 * Stone Private class
 */
public class CacheAction {

    public final ActionType type;
    public final StSwitchCache swCacheParams;
    public final Object value;

    public CacheAction(ActionType type, StSwitchCache swCacheParams, Object value) {
        this.type = type;
        this.swCacheParams = swCacheParams;
        this.value = value;
    }


    public static CacheAction getValueAction() {
        return new CacheAction(ActionType.GET_VALUE, null, null);
    }

    public static CacheAction setValueAction(Object value) {
        return new CacheAction(ActionType.SET_VALUE, null, value);
    }

    public static CacheAction setIfNullValueAction(Object value) {
        return new CacheAction(ActionType.SET_IF_NULL, null, value);
    }

    public static CacheAction switchCacheValueAction(StSwitchCache param) {
        return new CacheAction(ActionType.SWITCH_CACHE, param, null);
    }

    public static CacheAction switchCacheToDefAction() {
        return new CacheAction(ActionType.SWITCH_CACHE,
                new StSwitchCache(SwitchCache.CacheType.Default, 0, null),
                null
        );
    }

    public boolean isGetAction() {
        return type == ActionType.GET_VALUE;
    }

    public boolean isSetAction() {
        return type == ActionType.SET_VALUE;
    }

    public boolean isSetIfNullAction() {
        return type == ActionType.SET_IF_NULL;
    }

    public boolean isSwitchCacheAction() {
        return type == ActionType.SWITCH_CACHE;
    }


    public enum ActionType {
        GET_VALUE,
        SET_VALUE,
        SET_IF_NULL,
        SWITCH_CACHE,
    }

}


