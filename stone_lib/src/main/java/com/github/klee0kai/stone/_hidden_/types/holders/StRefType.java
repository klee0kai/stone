package com.github.klee0kai.stone._hidden_.types.holders;

import com.github.klee0kai.stone._hidden_.types.StListUtils;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public enum StRefType {
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

    public StRefType forList() {
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


    public StRefType forSingle() {
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

    public <T> StListUtils.IFormat<T, Reference<T>> formatter() {
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
