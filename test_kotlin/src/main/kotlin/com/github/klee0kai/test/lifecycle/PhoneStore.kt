package com.github.klee0kai.test.lifecycle

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.lifecycle.di.PComponent

object PhoneStore {
    val DI: PComponent = Stone.createComponent(PComponent::class.java)
}