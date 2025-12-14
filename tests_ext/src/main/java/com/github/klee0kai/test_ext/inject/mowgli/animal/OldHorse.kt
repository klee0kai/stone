package com.github.klee0kai.test_ext.inject.mowgli.animal

import com.github.klee0kai.test.mowgli.animal.Horse
import com.github.klee0kai.test.mowgli.identity.Ideology
import com.github.klee0kai.test_ext.inject.mowgli.diseases.Osteoarthritis
import com.github.klee0kai.test_ext.inject.mowgli.identity.OldKnowledge
import javax.inject.Inject

class OldHorse : Horse() {

    @Inject
    lateinit var oldKnowledge: OldKnowledge

    @Inject
    lateinit var osteoarthritis: Osteoarthritis

    @Inject
    override var ideology: Ideology? = null

}
