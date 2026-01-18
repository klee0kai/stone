package com.github.klee0kai.thekey.stone.ksp.exceptions

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSType

inline fun <T, R> T.wrapKsNoteInfo(
    ksNode: KSNode?,
    block: T.() -> R,
): R {
    return try {
        block()
    } catch (e: Throwable) {
        throw StoneException(
            message = "${e.message}. At ${ksNode?.location}  ",
            element = ksNode,
            cause = e,
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

fun Sequence<KSType>.forEachType(
    action: (index: Int, KSType) -> Unit
) {
    forEachIndexed { idx, type ->
        wrapKsNoteInfo(type.declaration) {
            action(idx, type)
        }
    }
}