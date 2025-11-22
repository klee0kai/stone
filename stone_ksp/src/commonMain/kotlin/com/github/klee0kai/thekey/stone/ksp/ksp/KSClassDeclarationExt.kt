package com.github.klee0kai.thekey.stone.ksp.ksp

import com.github.klee0kai.thekey.stone.ksp.utils.removeDoubles
import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName
import kotlin.reflect.KClass

fun KSClassDeclaration.findConstructor(
    parameters: List<KSType>,
): KSFunctionDeclaration? = getDeclaredFunctions().firstOrNull { function ->
    function.simpleName.asString() == "<init>"
            && function.parameters.all { it.type.resolve() in parameters || it.hasDefault }
}

fun KSDeclaration.isAnyType(
    vararg cl: KClass<*>,
) = cl.any { isType(it) }


fun KSDeclaration.isType(cl: KClass<*>): Boolean = qualifiedName?.asString() == cl.qualifiedName.toString()

fun KSDeclaration.isType(cl: ClassName): Boolean = qualifiedName?.asString() == cl.toString()

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