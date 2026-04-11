package com.github.klee0kai.test.core.di.dependecies

import com.github.klee0kai.stone.wrappers.AsyncLazy
import com.github.klee0kai.test.core.birds.*
import com.github.klee0kai.test.core.di.wrapper.CustomStoneProvide

interface BirdsDependencies {

    fun crow(): AsyncLazy<Crow>

    fun duck(): AsyncLazy<Duck>

    fun hen(): AsyncLazy<Hen>

    fun pigeon(): AsyncLazy<Pigeon>

    fun sparrow(): CustomStoneProvide<Sparrow>

    fun turkey(): CustomStoneProvide<Turkey>

}