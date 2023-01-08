package com.github.klee0kai.test_kotlin.tech.components

import com.github.klee0kai.test_kotlin.di.base_comp.qualifiers.KConnectType
import java.util.*

class Keyboard(
    val KConnectType: KConnectType
) {

    val uuid = UUID.randomUUID()

}