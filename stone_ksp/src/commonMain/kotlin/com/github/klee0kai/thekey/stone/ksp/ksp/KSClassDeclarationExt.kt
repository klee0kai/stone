package com.github.klee0kai.thekey.stone.ksp.ksp

import com.github.klee0kai.thekey.stone.ksp.utils.removeDoubles
import com.github.klee0kai.thekey.stone.ksp.utils.then
import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.symbol.*
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toClassNameOrNull
import kotlin.reflect.KClass


fun KSTypeReference.resolveAlias(): KSType = resolve().unwrapAlias()

fun KSTypeReference.resolveNotNullable(): KSType = resolve().makeNotNullable().unwrapAlias()

fun KSType.unwrapAlias(): KSType {
    var current: KSType = this
    while (current.declaration is KSTypeAlias) {
        val alias = current.declaration as KSTypeAlias
        current = alias.type.resolve()
    }
    return current
}


fun KSClassDeclaration.findConstructor(
    parameters: List<KSType>,
): KSFunctionDeclaration? = getDeclaredFunctions()
    .filter { function ->
        function.simpleName.asString() == "<init>"
                && function.parameters.all { it.type.resolveNotNullable() in parameters || it.hasDefault }
    }.maxByOrNull { it.parameters.size }

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
    allMethods.addAll(getDeclaredFunctions())

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

    yieldAll(
        allMethods
            .filter { it.simpleName.asString() !in exceptNames }
            .then(!allowDoubles) {
                removeDoubles { it1, it2 ->
                    it1.isSameMethods(it2)
                }
            }
    )
}


fun KSClassDeclaration.isChildOf(
    parentType: ClassName,
): Boolean {
    if (toClassName() == parentType) return true
    superTypes.forEach { type ->
        if (type.resolve().toClassNameOrNull() == type) return true
        if ((type.resolve().declaration as? KSClassDeclaration)?.isChildOf(parentType) == true) return true
    }
    return false
}

val KSType.isUnit: Boolean get() = declaration.qualifiedName?.asString() == "kotlin.Unit"

val KSType.isNotPrimitive: Boolean
    get() {
        return declaration.qualifiedName?.asString() !in setOf(
            "java.lang.Boolean",
            "java.lang.Byte",
            "java.lang.Short",
            "java.lang.Integer",
            "java.lang.Long",
            "java.lang.Character",
            "java.lang.Float",
            "java.lang.Double",
            "kotlin.Boolean",
            "kotlin.Byte",
            "kotlin.Short",
            "kotlin.Int",
            "kotlin.Long",
            "kotlin.Char",
            "kotlin.Float",
            "kotlin.Double",
            "kotlin.Unit",
        )
    }
