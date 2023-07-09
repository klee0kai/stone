package com.github.klee0kai.stone.helpers.itemholder;

import com.github.klee0kai.stone.annotations.component.GcSoftScope;
import com.github.klee0kai.stone.annotations.component.GcStrongScope;
import com.github.klee0kai.stone.annotations.component.GcWeakScope;
import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.stone.closed.types.single.ItemRefType;
import com.squareup.javapoet.ClassName;

public enum ItemCacheType {
    Strong, Soft, Weak;

    public ClassName getGcScopeClassName() {
        switch (this) {
            case Weak:
                return ClassName.get(GcWeakScope.class);
            case Strong:
                return ClassName.get(GcStrongScope.class);
            case Soft:
            default:
                return ClassName.get(GcSoftScope.class);
        }
    }

    public static ItemCacheType cacheTypeFrom(BindInstance.CacheType cacheType) {
        if (cacheType != null) switch (cacheType) {
            case Weak:
                return ItemCacheType.Weak;
            case Strong:
                return ItemCacheType.Strong;
            case Soft:
            default:
                return ItemCacheType.Soft;
        }
        return ItemCacheType.Soft;
    }

    public static ItemCacheType cacheTypeFrom(Provide.CacheType cacheType) {
        if (cacheType != null) switch (cacheType) {
            case Weak:
                return ItemCacheType.Weak;
            case Strong:
                return ItemCacheType.Strong;
            case Soft:
            default:
                return ItemCacheType.Soft;
        }
        return ItemCacheType.Soft;
    }

    public ItemRefType toRefTypeSingle() {
        switch (this) {
            case Strong:
                return ItemRefType.StrongObject;
            case Soft:
                return ItemRefType.SoftObject;
            case Weak:
                return ItemRefType.WeakObject;
            default:
                return null;
        }
    }

    public ItemRefType toRefTypeList() {
        switch (this) {
            case Strong:
                return ItemRefType.ListObject;
            case Soft:
                return ItemRefType.ListSoftObject;
            case Weak:
                return ItemRefType.ListWeakObject;
            default:
                return null;
        }
    }

}
