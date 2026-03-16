package com.github.klee0kai.thekey.stone.ksp.utils

inline fun <reified T> T.then(
    condition: Boolean,
    block: T.() -> T,
) = if (condition) {
    block()
} else {
    this
}
