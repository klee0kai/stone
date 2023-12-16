package com.github.klee0kai.wiki.wrapping;

import com.github.klee0kai.stone.wrappers.Ref;

import java.lang.ref.WeakReference;

public class CustomLazy<T> {

    private T value = null;
    private final WeakReference<T> call;

    public CustomLazy(Ref<T> call) {
        this.call = new WeakReference(call.get());
    }

    public T getValue() {
        if (value != null)
            return value;
        return value = call.get();
    }

}