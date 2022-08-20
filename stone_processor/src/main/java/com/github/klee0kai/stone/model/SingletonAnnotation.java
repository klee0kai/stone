package com.github.klee0kai.stone.model;

import com.github.klee0kai.stone.annotations.Singletone;

public class SingletonAnnotation implements Cloneable {

    public Singletone.CacheType cacheType = Singletone.CacheType.SOFT;

    public static SingletonAnnotation of(Singletone ann) {
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
