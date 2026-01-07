package com.github.klee0kai.test.feature1.di.dependencies

import com.github.klee0kai.stone.wrappers.AsyncCoroutineProvide
import com.github.klee0kai.test.core.di.wrapper.CustomStoneProvide
import com.github.klee0kai.test.feature1.berries.*

interface BerriesDependencies {

    fun berries(): CustomStoneProvide<Berries>

    fun cherry(): CustomStoneProvide<Cherry>

    fun currant(): AsyncCoroutineProvide<Currant>

    fun raspberry(): AsyncCoroutineProvide<Raspberry>

    fun strawberry(): AsyncCoroutineProvide<Strawberry>

}