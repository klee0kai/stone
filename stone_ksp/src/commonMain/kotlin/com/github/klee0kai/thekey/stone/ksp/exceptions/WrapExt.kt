package com.github.klee0kai.thekey.stone.ksp.exceptions

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSNode

fun <T, R> T.wrapKsNoteInfo(
    ksNode: KSNode?,
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

fun Sequence<KSFunctionDeclaration>.forEachFun(
    action: (index: Int, KSFunctionDeclaration) -> Unit
) {
    forEachIndexed { idx, func ->
        wrapKsNoteInfo(func) {
            action(idx, func)
        }
    }
}