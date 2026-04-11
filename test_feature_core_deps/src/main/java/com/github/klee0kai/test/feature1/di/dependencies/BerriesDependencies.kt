package com.github.klee0kai.test.feature1.di.dependencies

import com.github.klee0kai.stone.wrappers.AsyncLazy
import com.github.klee0kai.test.core.di.wrapper.CustomStoneProvide
import com.github.klee0kai.test.feature1.berries.*

interface BerriesDependencies {

    fun berries(): CustomStoneProvide<Berries>

    fun cherry(): CustomStoneProvide<Cherry>

    fun currant(): AsyncLazy<Currant>

    fun raspberry(): AsyncLazy<Raspberry>

    fun strawberry(): AsyncLazy<Strawberry>

}