package com.github.klee0kai.test.mowgli;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.base_forest.ForestComponent;

public class Forest {

    public static ForestComponent DI;


    public void create() {
        DI = Stone.createComponent(ForestComponent.class);
    }

}
