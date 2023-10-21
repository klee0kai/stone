package com.github.klee0kai.test.di.bindinstance.solarsystem;

import com.github.klee0kai.stone.wrappers.LazyProvide;
import com.github.klee0kai.test.mowgli.galaxy.Earth;
import com.github.klee0kai.test.mowgli.galaxy.Mercury;
import com.github.klee0kai.test.mowgli.galaxy.Saturn;
import com.github.klee0kai.test.mowgli.galaxy.Sun;

public interface SolarSystemDependencies {

    LazyProvide<Earth> earth(Sun sun);

    LazyProvide<Mercury> mercury(Sun sun);

    LazyProvide<Saturn> saturn(Sun sun);
}
