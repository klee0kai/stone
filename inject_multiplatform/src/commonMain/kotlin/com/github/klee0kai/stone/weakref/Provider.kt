package com.github.klee0kai.stone.weakref

expect interface Provider<T : Any?> {

    fun get(): T

}