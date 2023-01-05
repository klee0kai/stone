package com.github.klee0kai.test

import java.util.*

open class Context protected constructor() {
    open val uuid = UUID.randomUUID()

    companion object {
        fun create(): Context {
            return Context()
        }
    }
}