package com.github.klee0kai.test.feature1.di.dependencies

import com.github.klee0kai.stone.wrappers.AsyncProvide
import com.github.klee0kai.test.feature1.berries.*

interface BerriesDependencies {

    fun berries(): AsyncProvide<Berries>

    fun cherry(): AsyncProvide<Cherry>

    fun currant(): AsyncProvide<Currant>

    fun raspberry(): AsyncProvide<Raspberry>

    fun strawberry(): AsyncProvide<Strawberry>

}