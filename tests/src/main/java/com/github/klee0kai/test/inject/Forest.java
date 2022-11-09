package com.github.klee0kai.test.inject;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.inject.di.ForestComponent;

import java.util.UUID;

public class Forest {

    public static ForestComponent DI;


    public void create() {
        DI = Stone.createComponent(ForestComponent.class);
    }

}
