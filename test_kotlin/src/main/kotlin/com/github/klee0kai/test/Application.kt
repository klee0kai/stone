package com.github.klee0kai.test

import java.util.*

class Application protected constructor() : Context() {
    override var uuid = UUID.randomUUID()

    companion object {
        fun create(): Application {
            return Application()
        }
    }
}