package com.github.klee0kai.test.inject

import com.github.klee0kai.test.inject.forest.Blood
import com.github.klee0kai.test.inject.forest.Earth
import com.github.klee0kai.test.inject.forest.History
import com.github.klee0kai.test.inject.forest.IAnimal
import com.github.klee0kai.test.inject.identity.Conscience
import com.github.klee0kai.test.inject.identity.Knowledge
import javax.inject.Inject

class Snake : IAnimal {
    @Inject
    lateinit var blood: Blood

    @Inject
    lateinit var earth: Earth

    @Inject
    lateinit var history: History

    @Inject
    lateinit var conscience: Conscience

    @Inject
    lateinit var knowledge: Knowledge
    override fun born() {
        Forest.DI!!.inject(this)
    }
}