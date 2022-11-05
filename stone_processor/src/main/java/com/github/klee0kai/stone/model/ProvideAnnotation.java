package com.github.klee0kai.stone.model;

import com.github.klee0kai.stone.annotations.Provide;

public class ProvideAnnotation implements Cloneable {

    public Provide.CacheType cacheType = Provide.CacheType.SOFT;
    public String scope = "";

    public static ProvideAnnotation of(Provide ann) {
        if (ann == null)
            return null;
        ProvideAnnotation sAnn = new ProvideAnnotation();
        sAnn.cacheType = ann.cache();
//        sAnn.scope = ann.scope();
        return sAnn;
    }

    @Override
    public ProvideAnnotation clone() throws CloneNotSupportedException {
        return (ProvideAnnotation) super.clone();
    }
}
