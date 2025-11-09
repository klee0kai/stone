package com.github.klee0kai.stone.weakref

expect class WeakRef<T> constructor(value: T) {

    fun get(): T?

    fun clear()

}