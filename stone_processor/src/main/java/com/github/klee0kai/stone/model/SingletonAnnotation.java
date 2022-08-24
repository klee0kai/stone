package com.github.klee0kai.stone.model;

import com.github.klee0kai.stone.annotations.Singleton;

public class SingletonAnnotation implements Cloneable {

    public Singleton.CacheType cacheType = Singleton.CacheType.SOFT;

    public static SingletonAnnotation of(Singleton ann) {
        if (ann == null)
            return null;
        SingletonAnnotation sAnn = new SingletonAnnotation();
        sAnn.cacheType = ann.cache();
        return sAnn;
    }

    @Override
    public SingletonAnnotation clone() throws CloneNotSupportedException {
        return (SingletonAnnotation) super.clone();
    }
}
