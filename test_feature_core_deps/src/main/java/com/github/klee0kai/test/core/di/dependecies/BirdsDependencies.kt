package com.github.klee0kai.test.core.di.dependecies

import com.github.klee0kai.stone.wrappers.AsyncCoroutineProvide
import com.github.klee0kai.test.core.birds.*
import com.github.klee0kai.test.core.di.wrapper.CustomStoneProvide

interface BirdsDependencies {

    fun crow(): AsyncCoroutineProvide<Crow>

    fun duck(): AsyncCoroutineProvide<Duck>

    fun hen(): AsyncCoroutineProvide<Hen>

    fun pigeon(): AsyncCoroutineProvide<Pigeon>

    fun sparrow(): CustomStoneProvide<Sparrow>

    fun turkey(): CustomStoneProvide<Turkey>

}