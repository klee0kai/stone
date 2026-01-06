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
        if (parameters[idx].type.resolve() != other.parameters[idx].type.resolve()) {
            return false
        }
    }

    return true
}

fun KSFunctionDeclaration.joinInvokeArguments(
    availableVariables: List<KSValueParameter>,
): String {
    return parameters.mapNotNull { parameter ->
        val availableVariable = availableVariables
            .firstOrNull { it.type.resolveNotNullable() == parameter.type.resolveNotNullable() }
        if (availableVariable != null) {
            val notNullablePostFix = if (availableVariable.type.resolve().isMarkedNullable
                && !parameter.type.resolve().isMarkedNullable
            ) {
                "!!"
            } else {
                ""
            }

            "${parameter.name!!.asString()} = ${availableVariable.name!!.asString()}${notNullablePostFix}"
        } else {
            null
        }
    }.joinToString(", ")
}

fun KSFunctionDeclaration.isClassReturn(
): Boolean = returnType?.resolve()?.isNotPrimitive ?: false
