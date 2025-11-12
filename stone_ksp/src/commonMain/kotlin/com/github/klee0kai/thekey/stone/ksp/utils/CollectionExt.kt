package com.github.klee0kai.thekey.stone.ksp.utils

import java.util.*


fun <T> List<T>.removeDoubles(
    compare: (T, T) -> Boolean,
): LinkedList<T> {
    val out = LinkedList<T>()
    for (item in this) {
        val contains = out.any { compare.invoke(item, it) }
        if (!contains) out.add(item)
    }
    return out
}