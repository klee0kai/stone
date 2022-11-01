package com.github.klee0kai.stone.model;

import com.github.klee0kai.stone.annotations.BindInstance;
import com.github.klee0kai.stone.annotations.Provide;

public class ChangeableSingletonAnnotation implements Cloneable {

    public BindInstance.CacheType cacheType = BindInstance.CacheType.SOFT;
    public String scope = "";

    public static ChangeableSingletonAnnotation of(BindInstance ann) {
        if (ann == null)
            return null;
        ChangeableSingletonAnnotation sAnn = new ChangeableSingletonAnnotation();
        sAnn.cacheType = ann.cache();
//        sAnn.scope = ann.scope();
        return sAnn;
    }

    @Override
    public ChangeableSingletonAnnotation clone() throws CloneNotSupportedException {
        return (ChangeableSingletonAnnotation) super.clone();
    }
}
