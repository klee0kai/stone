package com.github.klee0kai.stone.model.annotations;

import com.github.klee0kai.stone.annotations.module.BindInstance;

public class BindInstanceAnnotation implements Cloneable {

    public BindInstance.CacheType cacheType = BindInstance.CacheType.Soft;

    public static BindInstanceAnnotation of(BindInstance ann) {
        if (ann == null)
            return null;
        BindInstanceAnnotation sAnn = new BindInstanceAnnotation();
        sAnn.cacheType = ann.cache();
        return sAnn;
    }


    @Override
    public BindInstanceAnnotation clone() throws CloneNotSupportedException {
        return (BindInstanceAnnotation) super.clone();
    }
}
