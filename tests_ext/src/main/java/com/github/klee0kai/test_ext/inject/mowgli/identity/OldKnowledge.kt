package com.github.klee0kai.test_ext.inject.mowgli.identity

import com.github.klee0kai.test.mowgli.identity.Knowledge

class OldKnowledge : Knowledge() {

    fun doChildKnowledge(): Boolean {
        return true
    }

    override val isOldKnowledge: Boolean
        get() = true

}
