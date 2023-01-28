package com.github.klee0kai.stone.closed.types;

import com.github.klee0kai.stone.annotations.component.SwitchCache;

public class SwitchCacheParam {

    public final SwitchCache.CacheType cache;
    public final long time;
    public final TimeScheduler scheduler;

    public SwitchCacheParam(SwitchCache.CacheType cache, long time, TimeScheduler scheduler) {
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
