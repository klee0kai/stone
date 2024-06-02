package com.github.klee0kai.test.app.di.dependencies

import com.github.klee0kai.test.app.mushrooms.Amanita
import com.github.klee0kai.test.app.mushrooms.Cep
import com.github.klee0kai.test.app.mushrooms.Champignon
import com.github.klee0kai.test.app.mushrooms.Russule
import com.github.klee0kai.test.core.di.wrapper.CustomStoneProvide

interface MushroomsDependencies {

    fun cep(): CustomStoneProvide<Cep>

    fun amanita(): CustomStoneProvide<Amanita>

    fun campignon(): CustomStoneProvide<Champignon>

    fun russule(): CustomStoneProvide<Russule>


}