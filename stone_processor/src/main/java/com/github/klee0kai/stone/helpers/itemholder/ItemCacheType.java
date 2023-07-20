package com.github.klee0kai.stone.helpers.itemholder;

import com.github.klee0kai.stone._hidden_.types.holders.StoneRefType;
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

    public StoneRefType toRefTypeSingle() {
        switch (this) {
            case Strong:
                return StoneRefType.StrongObject;
            case Soft:
                return StoneRefType.SoftObject;
            case Weak:
                return StoneRefType.WeakObject;
            default:
                return null;
        }
    }

    public StoneRefType toRefTypeList() {
        switch (this) {
            case Strong:
                return StoneRefType.ListObject;
            case Soft:
                return StoneRefType.ListSoftObject;
            case Weak:
                return StoneRefType.ListWeakObject;
            default:
                return null;
        }
    }

}
