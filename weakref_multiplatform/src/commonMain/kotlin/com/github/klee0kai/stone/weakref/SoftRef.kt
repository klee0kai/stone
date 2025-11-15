package com.github.klee0kai.stone.weakref

expect class SoftRef<T : Any?> constructor(value: T) : Ref<T?> {

    override fun get(): T?

    fun clear()

}