@file:OptIn(KspExperimental::class)

package com.github.klee0kai.thekey.stone.ksp.helpers

import com.github.klee0kai.stone.annotations.component.*
import com.github.klee0kai.stone.weakref.Named
import com.github.klee0kai.stone.weakref.Qualifier
import com.github.klee0kai.thekey.stone.ksp.helpers.annotations.findComponentAnnotation
import com.github.klee0kai.thekey.stone.ksp.ksp.isType
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName

fun Resolver.findComponentForModuleOrDep(
    moduleCl: ClassName,
): Sequence<KSClassDeclaration> {
    return getSymbolsWithAnnotation(Component::class.asClassName().canonicalName)
        .filterIsInstance<KSClassDeclaration>()
        .filter { componentCl ->
            componentCl.getAllFunctions().any {
                it.returnType?.resolve()?.declaration?.isType(moduleCl) ?: false
            }
        }
}

val KSClassDeclaration.allIdentifierTypes: Sequence<KSType>
    get() {
        val componentCl = this@allIdentifierTypes
        val allParentsSequence = (sequenceOf(componentCl) + superTypes)
        return allParentsSequence
            .flatMap { it.findComponentAnnotation() }
            .flatMap { it.identifiers }
    }

val KSClassDeclaration.wrapperProviders: Sequence<KSType>
    get() {
        val componentCl = this@wrapperProviders
        val allParentsSequence = (sequenceOf(componentCl) + superTypes)
        return allParentsSequence
            .flatMap { it.findComponentAnnotation() }
            .flatMap { it.wrapperProviders }
    }

val KSAnnotated.scopeAnnotations: Sequence<KSAnnotation>
    get() {
        val standardScopeAnnotations = listOf(
            GcAllScope::class, GcWeakScope::class,
            GcSoftScope::class, GcStrongScope::class
        )

        return annotations.filter { funAnnotation ->
            standardScopeAnnotations.any { funAnnotation.annotationType.resolve().declaration.isType(it) }
                    || funAnnotation.annotationType.resolve().annotations.any { annotationOfAnnotation ->
                annotationOfAnnotation.annotationType.resolve().declaration.isType(GcScopeAnnotation::class)
            }
        }
    }

val KSAnnotated.qualifierAnnotations: Sequence<KSAnnotation>
    get() {
        val standardQualifierAnnotations = listOf(
            Named::class
        )

        return annotations.filter { funAnnotation ->
            standardQualifierAnnotations.any { funAnnotation.annotationType.resolve().declaration.isType(it) }
                    || funAnnotation.annotationType.resolve().annotations.any { annotationOfAnnotation ->
                annotationOfAnnotation.annotationType.resolve().declaration.isType(Qualifier::class)
            }
        }
    }


fun KSAnnotation.isSameAsQualifier(
    ann: KSAnnotation
): Boolean {
    if (annotationType.resolve().toClassName() != ann.annotationType.resolve().toClassName()) return false
    val annArguments1 = arguments.map { it.name?.asString() to it.value }.sortedBy { it.first }
    val annArguments2 = ann.arguments.map { it.name?.asString() to it.value }.sortedBy { it.first }
    return annArguments1 == annArguments2
}

