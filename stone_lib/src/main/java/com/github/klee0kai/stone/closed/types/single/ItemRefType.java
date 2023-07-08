package com.github.klee0kai.stone.closed.types.single;

import com.github.klee0kai.stone.closed.types.ListUtils;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public enum ItemRefType {
    StrongObject,
    WeakObject,
    SoftObject,
    ListObject,
    ListWeakObject,
    ListSoftObject;

    public boolean isList() {
        switch (this) {
            case StrongObject:
            case WeakObject:
            case SoftObject:
                return false;
            case ListObject:
            case ListWeakObject:
            case ListSoftObject:
                return true;
        }
        return false;
    }

    public ItemRefType forList() {
        switch (this) {
            case StrongObject:
                return ListObject;
            case WeakObject:
                return ListWeakObject;
            case SoftObject:
                return ListSoftObject;
            default:
                return this;
        }
    }


    public ItemRefType forSingle() {
        switch (this) {
            case ListObject:
                return StrongObject;
            case ListWeakObject:
                return WeakObject;
            case ListSoftObject:
                return SoftObject;
            default:
                return this;
        }
    }

    public <T> ListUtils.IFormat<T, Reference<T>> formatter() {
        switch (this) {
            case WeakObject:
            case ListWeakObject:
                return WeakReference::new;
            case SoftObject:
            case ListSoftObject:
                return SoftReference::new;
            default:
                return null;
        }
    }


}
