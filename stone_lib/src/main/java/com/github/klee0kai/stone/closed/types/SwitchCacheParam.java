package com.github.klee0kai.stone.closed.types;

import com.github.klee0kai.stone.annotations.component.SwitchCache;
import com.github.klee0kai.stone.closed.types.holders.StTimeScheduler;

/**
 * Stone Private class
 */
public class SwitchCacheParam {

    public final SwitchCache.CacheType cache;
    public final long time;
    public final StTimeScheduler scheduler;

    public SwitchCacheParam(SwitchCache.CacheType cache, long time, StTimeScheduler scheduler) {
        this.cache = cache;
        this.time = time;
        this.scheduler = scheduler;
    }

    public static SwitchCacheParam toWeak() {
        return new SwitchCacheParam(SwitchCache.CacheType.Weak, -1, null);
    }

    public static SwitchCacheParam toDef() {
        return new SwitchCacheParam(SwitchCache.CacheType.Default, -1, null);
    }

}
