package com.github.klee0kai.thekey.stone.ksp.ksp

import com.github.klee0kai.thekey.stone.ksp.utils.removeDoubles
import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter


fun KSClassDeclaration.findConstructor(
    parameters: List<KSValueParameter>,
): KSFunctionDeclaration? = getDeclaredFunctions().firstOrNull { function ->
    function.simpleName.asString() == "<init>"
            && parameters.map { it.type } == function.parameters.map { it.type }
}


fun KSClassDeclaration.getAllMethods(
    includeObjectMethods: Boolean = false,
    allowDoubles: Boolean = false,
    vararg exceptNames: String = emptyArray(),
): Sequence<KSFunctionDeclaration> = sequence<KSFunctionDeclaration> {
    val cl = this@getAllMethods
    if (!includeObjectMethods && cl.qualifiedName?.asString() in listOf(
            Object::class.qualifiedName,
            Any::class.qualifiedName
        )
    ) {
        return@sequence
    }

    val allMethods = mutableListOf<KSFunctionDeclaration>()
    getAllSuperTypes().forEach { superType ->
        allMethods.addAll(
            (superType.declaration as KSClassDeclaration)
                .getAllMethods(
                    includeObjectMethods = includeObjectMethods,
                    allowDoubles = allowDoubles,
                    exceptNames = exceptNames,
                )
        )
    }
    allMethods.addAll(getDeclaredFunctions())

    yieldAll(
        allMethods
            .filter {
                it.simpleName.asString() !in exceptNames
            }
            .removeDoubles { it1, it2 ->
                it1.isSameMethods(it2)
            }
    )
}