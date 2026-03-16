package com.github.klee0kai.test.core.di.wrapper

import com.github.klee0kai.stone.weakref.Ref


class CustomStoneProvide<T>(val call: Ref<T>) {

    fun get(): T = call.get()

}