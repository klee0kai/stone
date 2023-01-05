package com.github.klee0kai.test.inject

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.inject.di.ForestComponent

class Forest {
    fun create() {
        DI = Stone.createComponent(
            ForestComponent::class.java
        )
    }

    companion object {
        var DI: ForestComponent? = null
    }
}