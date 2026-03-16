package com.github.klee0kai.test.mowgli.animal

import com.github.klee0kai.stone.Inject
import com.github.klee0kai.test.mowgli.body.Blood
import com.github.klee0kai.test.mowgli.community.History
import com.github.klee0kai.test.mowgli.galaxy.Earth
import com.github.klee0kai.test.mowgli.identity.Conscience
import com.github.klee0kai.test.mowgli.identity.Ideology
import com.github.klee0kai.test.mowgli.identity.Knowledge

open class Horse : IAnimal {

    @Inject
    var blood: Blood? = null

    @Inject
    var earth: Earth? = null

    @Inject
    var history: History? = null

    @Inject
    var conscience: Conscience? = null

    @Inject
    var knowledge: Knowledge? = null

    @Inject
    open var ideology: Ideology? = null

    var methodInjectedConscience: Conscience? = null

    var methodInjectedKnowledge: Knowledge? = null

    @Inject
    fun initInject(conscience: Conscience?, knowledge: Knowledge?) {
        methodInjectedConscience = conscience
        methodInjectedKnowledge = knowledge
    }
}
