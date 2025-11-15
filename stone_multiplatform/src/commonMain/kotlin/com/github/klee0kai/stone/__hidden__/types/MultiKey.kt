package com.github.klee0kai.stone.__hidden__.types

/**
 * Stone Private class
 */
class MultiKey(vararg subKeys: Any?) {

    private val subKeys = mutableListOf<Any?>()

    init {
        this.subKeys.addAll(subKeys)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as MultiKey

        return subKeys == other.subKeys
    }

    override fun hashCode(): Int {
        return subKeys.hashCode()
    }


}
