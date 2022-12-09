package com.github.klee0kai.stone.model.annotations;

import com.github.klee0kai.stone.annotations.component.SwitchCache;

public class SwitchCacheAnnotation implements Cloneable {

    public SwitchCache.CacheType cache;
    public long timeMillis;

    public static SwitchCacheAnnotation of(SwitchCache ann) {
        if (ann == null)
            return null;
        SwitchCacheAnnotation sAnn = new SwitchCacheAnnotation();
        sAnn.timeMillis = ann.timeMillis();
        sAnn.cache = ann.cache();
        return sAnn;
    }

    @Override
    public SwitchCacheAnnotation clone() throws CloneNotSupportedException {
        return (SwitchCacheAnnotation) super.clone();
    }
}
