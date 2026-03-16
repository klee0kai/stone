package com.github.klee0kai.test.di.bindinstance.solarsystem

import com.github.klee0kai.stone.wrappers.LazyProvide
import com.github.klee0kai.test.mowgli.galaxy.Earth
import com.github.klee0kai.test.mowgli.galaxy.Mercury
import com.github.klee0kai.test.mowgli.galaxy.Saturn

interface SolarSystemDependencies {
    fun earth(): LazyProvide<Earth?>?

    fun mercury(): LazyProvide<Mercury?>?

    fun saturn(): LazyProvide<Saturn?>?
}
