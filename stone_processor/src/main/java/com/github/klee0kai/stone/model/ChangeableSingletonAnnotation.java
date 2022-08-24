package com.github.klee0kai.stone.model;

import com.github.klee0kai.stone.annotations.ChangeableSingleton;
import com.github.klee0kai.stone.annotations.Singleton;

public class ChangeableSingletonAnnotation implements Cloneable {

    public Singleton.CacheType cacheType = Singleton.CacheType.SOFT;

    public static ChangeableSingletonAnnotation of(ChangeableSingleton ann) {
        if (ann == null)
            return null;
        ChangeableSingletonAnnotation sAnn = new ChangeableSingletonAnnotation();
        sAnn.cacheType = ann.cache();
        return sAnn;
    }

    @Override
    public ChangeableSingletonAnnotation clone() throws CloneNotSupportedException {
        return (ChangeableSingletonAnnotation) super.clone();
    }
}
