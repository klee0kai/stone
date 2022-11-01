package com.github.klee0kai.stone.model;

import com.github.klee0kai.stone.annotations.Provide;

public class SingletonAnnotation implements Cloneable {

    public Provide.CacheType cacheType = Provide.CacheType.SOFT;
    public String scope = "";

    public static SingletonAnnotation of(Provide ann) {
        if (ann == null)
            return null;
        SingletonAnnotation sAnn = new SingletonAnnotation();
        sAnn.cacheType = ann.cache();
//        sAnn.scope = ann.scope();
        return sAnn;
    }

    @Override
    public SingletonAnnotation clone() throws CloneNotSupportedException {
        return (SingletonAnnotation) super.clone();
    }
}
