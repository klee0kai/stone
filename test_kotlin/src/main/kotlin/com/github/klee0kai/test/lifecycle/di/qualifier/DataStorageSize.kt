package com.github.klee0kai.test.lifecycle.di.qualifier

import java.util.*

class DataStorageSize(var size: String) {
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as DataStorageSize
        return size == that.size
    }

    override fun hashCode(): Int {
        return Objects.hash(size)
    }
}