package com.github.klee0kai.stone.wrappers

import com.github.klee0kai.stone.weakref.Ref
import kotlin.reflect.KProperty

operator fun <T> Ref<T>.getValue(t: Any?, property: KProperty<*>): T = get()!!
