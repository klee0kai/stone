package com.github.klee0kai.test_ext.inject.mowgli.identity

import com.github.klee0kai.test.mowgli.identity.Ideology
import java.util.*

class FamilyIdeology : Ideology() {
    override var uuid: UUID = UUID.randomUUID()


    override val isFamilyIdeology: Boolean
        get() = true
}
