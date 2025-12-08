package com.github.klee0kai.test.mowgli.animal

import com.github.klee0kai.test.mowgli.body.Blood
import com.github.klee0kai.test.mowgli.community.History
import com.github.klee0kai.test.mowgli.galaxy.Earth
import com.github.klee0kai.test.mowgli.identity.Conscience
import com.github.klee0kai.test.mowgli.identity.Ideology
import com.github.klee0kai.test.mowgli.identity.Knowledge
import javax.inject.Inject

open class Horse : IAnimal {
    @JvmField
    @Inject
    var blood: Blood? = null

    @JvmField
    @Inject
    var earth: Earth? = null

    @JvmField
    @Inject
    var history: History? = null

    @JvmField
    @Inject
    var conscience: Conscience? = null

    @JvmField
    @Inject
    var knowledge: Knowledge? = null

    @JvmField
    @Inject
    var ideology: Ideology? = null


    @JvmField
    var methodInjectedConscience: Conscience? = null
    @JvmField
    var methodInjectedKnowledge: Knowledge? = null

    @Inject
    fun initInject(conscience: Conscience?, knowledge: Knowledge?) {
        methodInjectedConscience = conscience
        methodInjectedKnowledge = knowledge
    }
}
