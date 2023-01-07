package com.github.klee0kai.test.inject

import com.github.klee0kai.test.inject.forest.Blood
import com.github.klee0kai.test.inject.forest.Earth
import com.github.klee0kai.test.inject.forest.History
import com.github.klee0kai.test.inject.forest.IAnimal
import com.github.klee0kai.test.inject.identity.Conscience
import com.github.klee0kai.test.inject.identity.Ideology
import com.github.klee0kai.test.inject.identity.Knowledge
import javax.inject.Inject

class Horse : IAnimal {
    @Inject
    var blood: Blood? = null

    @Inject
    var earth: Earth? = null

    @Inject
    var history: History? = null

    @Inject
    lateinit var conscience: Conscience

    @Inject
    lateinit var knowledge: Knowledge

    @Inject
    var ideology: Ideology? = null
    override fun born() {
        Forest.DI!!.inject(this) { }
    }
}