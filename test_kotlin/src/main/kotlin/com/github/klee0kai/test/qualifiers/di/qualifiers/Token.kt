package com.github.klee0kai.test.qualifiers.di.qualifiers

import java.util.*

class Token(var token: String?) {
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val token1 = o as Token
        return token == token1.token
    }

    override fun hashCode(): Int {
        return Objects.hash(token)
    }
}