@file:OptIn(ExperimentalUuidApi::class)

package com.github.klee0kai.test.mowgli.community

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class History {
    var uuid: String = Uuid.random().toString()
}
