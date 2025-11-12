package com.github.klee0kai.thekey.stone.ksp.ksp

import com.google.devtools.ksp.symbol.KSFunctionDeclaration

fun KSFunctionDeclaration.isSameMethods(
    other: KSFunctionDeclaration,
): Boolean {
    if (simpleName != other.simpleName
        || parameters.size != other.parameters.size
    ) {
        return false
    }
    for (idx in parameters.indices) {
        if (parameters[idx].type != other.parameters[idx].type) {
            return false
        }
    }

    return true
}