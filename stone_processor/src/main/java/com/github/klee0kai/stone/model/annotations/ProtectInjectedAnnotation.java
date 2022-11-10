package com.github.klee0kai.stone.model.annotations;

import com.github.klee0kai.stone.annotations.component.ProtectInjected;

public class ProtectInjectedAnnotation implements Cloneable {

    public long timeMillis;

    public static ProtectInjectedAnnotation of(ProtectInjected ann) {
        if (ann == null)
            return null;
        ProtectInjectedAnnotation sAnn = new ProtectInjectedAnnotation();
        sAnn.timeMillis = ann.timeMillis();
        return sAnn;
    }

    @Override
    public ProtectInjectedAnnotation clone() throws CloneNotSupportedException {
        return (ProtectInjectedAnnotation) super.clone();
    }
}
