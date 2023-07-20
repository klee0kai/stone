package com.github.klee0kai.stone.helpers.itemholder;

import com.github.klee0kai.stone._hidden_.types.holders.StRefType;
import com.github.klee0kai.stone.annotations.component.GcSoftScope;
import com.github.klee0kai.stone.annotations.component.GcStrongScope;
import com.github.klee0kai.stone.annotations.component.GcWeakScope;
import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.stone.annotations.module.Provide;
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

    public StRefType toRefTypeSingle() {
        switch (this) {
            case Strong:
                return StRefType.StrongObject;
            case Soft:
                return StRefType.SoftObject;
            case Weak:
                return StRefType.WeakObject;
            default:
                return null;
        }
    }

    public StRefType toRefTypeList() {
        switch (this) {
            case Strong:
                return StRefType.ListObject;
            case Soft:
                return StRefType.ListSoftObject;
            case Weak:
                return StRefType.ListWeakObject;
            default:
                return null;
        }
    }

}
