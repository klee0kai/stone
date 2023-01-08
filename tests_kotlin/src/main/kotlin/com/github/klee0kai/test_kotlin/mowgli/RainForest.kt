package com.github.klee0kai.test_kotlin.mowgli

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test_kotlin.di.base_forest.RainForestComponent

class RainForest {

    companion object {
        lateinit var DI: RainForestComponent
    }

    fun create() {
        DI = Stone.createComponent(RainForestComponent::class.java)
    }


}