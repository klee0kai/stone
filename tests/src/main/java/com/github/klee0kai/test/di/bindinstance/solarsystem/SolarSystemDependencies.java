package com.github.klee0kai.test.di.bindinstance.solarsystem;

import com.github.klee0kai.stone.wrappers.LazyProvide;
import com.github.klee0kai.test.mowgli.galaxy.Earth;
import com.github.klee0kai.test.mowgli.galaxy.Mercury;
import com.github.klee0kai.test.mowgli.galaxy.Saturn;

public interface SolarSystemDependencies {

    LazyProvide<Earth> earth();

    LazyProvide<Mercury> mercury();

    LazyProvide<Saturn> saturn();
}
