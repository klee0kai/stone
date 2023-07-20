package com.github.klee0kai.stone.closed.types;

import com.github.klee0kai.stone.annotations.component.SwitchCache;
import com.github.klee0kai.stone.closed.types.holders.StTimeScheduler;

/**
 * Stone Private class
 */
public class StSwitchCache {

    public final SwitchCache.CacheType cache;
    public final long time;
    public final StTimeScheduler scheduler;

    public StSwitchCache(SwitchCache.CacheType cache, long time, StTimeScheduler scheduler) {
        this.cache = cache;
        this.time = time;
        this.scheduler = scheduler;
    }

    public static StSwitchCache toWeak() {
        return new StSwitchCache(SwitchCache.CacheType.Weak, -1, null);
    }

    public static StSwitchCache toDef() {
        return new StSwitchCache(SwitchCache.CacheType.Default, -1, null);
    }

}
