package com.github.klee0kai.test_ext.inject;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.inject.Forest;
import com.github.klee0kai.test_ext.inject.di.OldForestComponent;

public class OldForest extends Forest {

    public static OldForestComponent DIPro;


    public void create() {
        super.create();

    }

    /**
     * emulate dynamic feature load
     */
    public void old() {
        DIPro = Stone.createComponent(OldForestComponent.class);
        DIPro.extOf(Forest.DI);
    }

}