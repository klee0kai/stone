package com.github.klee0kai.stone._hidden_.types;

import com.github.klee0kai.stone.annotations.component.SwitchCache;

/**
 * Stone Private class
 */
public class StCacheAction {

    public final ActionType type;
    public final StSwitchCache swCacheParams;
    public final Object value;

    public StCacheAction(ActionType type, StSwitchCache swCacheParams, Object value) {
        this.type = type;
        this.swCacheParams = swCacheParams;
        this.value = value;
    }


    public static StCacheAction getValueAction() {
        return new StCacheAction(ActionType.GET_VALUE, null, null);
    }

    public static StCacheAction setValueAction(Object value) {
        return new StCacheAction(ActionType.SET_VALUE, null, value);
    }

    public static StCacheAction setIfNullValueAction(Object value) {
        return new StCacheAction(ActionType.SET_IF_NULL, null, value);
    }

    public static StCacheAction switchCacheValueAction(StSwitchCache param) {
        return new StCacheAction(ActionType.SWITCH_CACHE, param, null);
    }

    public static StCacheAction switchCacheToDefAction() {
        return new StCacheAction(ActionType.SWITCH_CACHE,
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


