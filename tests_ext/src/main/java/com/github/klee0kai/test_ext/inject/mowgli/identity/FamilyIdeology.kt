@file:OptIn(ExperimentalUuidApi::class)

package com.github.klee0kai.test_ext.inject.mowgli.identity

import com.github.klee0kai.test.mowgli.identity.Ideology
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class FamilyIdeology : Ideology() {
    override var uuid: String = Uuid.random().toString()

    override val isFamilyIdeology: Boolean
        get() = true

}
