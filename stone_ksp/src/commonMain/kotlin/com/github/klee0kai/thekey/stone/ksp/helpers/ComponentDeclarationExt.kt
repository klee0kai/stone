@file:OptIn(KspExperimental::class)

package com.github.klee0kai.thekey.stone.ksp.helpers

import com.github.klee0kai.stone.annotations.component.*
import com.github.klee0kai.thekey.stone.ksp.ksp.isType
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSTypeReference
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asClassName
import kotlin.reflect.KClass

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


val KSClassDeclaration.allIdentifierTypes: Sequence<KClass<*>>
    get() {
        val componentCl = this@allIdentifierTypes as? KSTypeReference
        val allParentsSequence = (sequenceOf(componentCl) + superTypes)
        return allParentsSequence
            .flatMap { it?.getAnnotationsByType(Component::class) ?: emptySequence() }
            .flatMap { it.identifiers.asSequence() }
    }

val KSClassDeclaration.wrapperProviders: Sequence<KClass<*>>
    get() {
        val componentCl = this@wrapperProviders as? KSTypeReference
        val allParentsSequence = (sequenceOf(componentCl) + superTypes)
        return allParentsSequence
            .flatMap { it?.getAnnotationsByType(Component::class) ?: emptySequence() }
            .flatMap { it.wrapperProviders.asSequence() }
    }

val KSFunctionDeclaration.scopeAnnotations: Sequence<KSAnnotation>
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