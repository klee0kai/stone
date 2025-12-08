package com.github.klee0kai.thekey.stone.ksp.exceptions

import com.google.devtools.ksp.symbol.KSNode

fun <T, R> T.wrapKsNoteInfo(
    ksNode: KSNode,
    block: T.() -> R,
): R {
    return try {
        block()
    } catch (e: Throwable) {
        throw StoneException(
            message = e.message,
            element = ksNode,
        )
    }
}