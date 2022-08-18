package com.klee0kai.stone.model;

import com.klee0kai.stone.annotations.Item;

import javax.lang.model.element.AnnotationMirror;

public class ItemAnnotation implements Cloneable {

    public Item.CacheType cacheType;

    public static ItemAnnotation of(Item ann) {
        if (ann == null)
            return null;
        ItemAnnotation itAnn = new ItemAnnotation();
        itAnn.cacheType = ann.cache();
        return itAnn;
    }

    @Override
    public ItemAnnotation clone() throws CloneNotSupportedException {
        return (ItemAnnotation) super.clone();
    }
}
