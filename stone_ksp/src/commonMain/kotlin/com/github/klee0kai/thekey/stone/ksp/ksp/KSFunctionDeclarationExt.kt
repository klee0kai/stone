package com.github.klee0kai.thekey.stone.ksp.ksp

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter

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

fun KSFunctionDeclaration.joinInvokeArguments(
    availableVariables: List<KSValueParameter>,
): String {
    return parameters.mapNotNull { parameter ->
        val availableVariable = availableVariables.firstOrNull { it.type.resolve() == parameter.type.resolve() }
        if (availableVariable != null) {
            "${parameter.name!!.asString()} = ${availableVariable.name!!.asString()}"
        } else {
            null
        }
    }.joinToString(", ")
}

fun KSFunctionDeclaration.isClassReturn(
): Boolean = returnType?.resolve()?.isNotPrimitive ?: false
