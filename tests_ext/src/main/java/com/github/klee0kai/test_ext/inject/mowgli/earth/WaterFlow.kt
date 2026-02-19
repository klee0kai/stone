@file:OptIn(ExperimentalUuidApi::class)

package com.github.klee0kai.test_ext.inject.mowgli.earth

import com.github.klee0kai.test.mowgli.earth.River
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class WaterFlow : River() {
    override var uuid: String = Uuid.random().toString()
}
