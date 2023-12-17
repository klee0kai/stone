package com.github.klee0kai.wiki.cachecontrol;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.mowgli.galaxy.Mercury;

import javax.inject.Inject;

public class SolarSystem {

    static PlanetsComponent DI = Stone.createComponent(PlanetsComponent.class);

    @Inject
    Mercury mercury;

    void create() {
        DI.inject(this);
    }

    void beforeRecreate() {
        DI.protectInjected(this);
    }

}
