package com.github.klee0kai.test.app.di.dependencies

import com.github.klee0kai.test.app.birds.Pelican
import com.github.klee0kai.test.core.di.wrapper.CustomStoneProvide

interface AppBirdsDependencies {

    fun pelican(): CustomStoneProvide<Pelican>

}