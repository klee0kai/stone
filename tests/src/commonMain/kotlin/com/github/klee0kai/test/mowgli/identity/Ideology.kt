@file:OptIn(ExperimentalUuidApi::class)

package com.github.klee0kai.test.mowgli.identity

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

open class Ideology {

    open var uuid: String = Uuid.random().toString()

    open val isFamilyIdeology: Boolean get() = false

}
