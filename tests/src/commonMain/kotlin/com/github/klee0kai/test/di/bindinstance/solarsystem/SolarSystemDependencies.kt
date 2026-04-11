package com.github.klee0kai.test.di.bindinstance.solarsystem

import com.github.klee0kai.stone.wrappers.LazyProvider
import com.github.klee0kai.test.mowgli.galaxy.Earth
import com.github.klee0kai.test.mowgli.galaxy.Mercury
import com.github.klee0kai.test.mowgli.galaxy.Saturn

interface SolarSystemDependencies {
    fun earth(): LazyProvider<Earth?>?

    fun mercury(): LazyProvider<Mercury?>?

    fun saturn(): LazyProvider<Saturn?>?
}
