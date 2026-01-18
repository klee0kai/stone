package com.github.klee0kai.stone.weakref

fun interface Ref<T>  {

    fun get(): T

}