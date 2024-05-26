package com.github.klee0kai.test.core.di.dependecies

import com.github.klee0kai.stone.wrappers.AsyncProvide
import com.github.klee0kai.test.core.birds.*
import com.github.klee0kai.test.core.di.wrapper.CustomStoneProvide

interface BirdsDependencies {

    fun crow(): AsyncProvide<Crow>

    fun duck(): AsyncProvide<Duck>

    fun hen(): AsyncProvide<Hen>

    fun pigeon(): AsyncProvide<Pigeon>

    fun sparrow(): CustomStoneProvide<Sparrow>

    fun turkey(): CustomStoneProvide<Turkey>

}