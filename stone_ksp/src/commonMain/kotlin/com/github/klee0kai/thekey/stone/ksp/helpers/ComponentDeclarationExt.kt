@file:OptIn(KspExperimental::class)

package com.github.klee0kai.thekey.stone.ksp.helpers

import com.github.klee0kai.stone.annotations.component.*
import com.github.klee0kai.stone.annotations.qualifier.IgnoreQualifier
import com.github.klee0kai.stone.lifecycle.StoneLifeCycleOwner
import com.github.klee0kai.stone.weakref.Named
import com.github.klee0kai.stone.weakref.Qualifier
import com.github.klee0kai.stone.weakref.Scope
import com.github.klee0kai.thekey.stone.ksp.helpers.annotations.findComponentAnnotation
import com.github.klee0kai.thekey.stone.ksp.ksp.isAnyType
import com.github.klee0kai.thekey.stone.ksp.ksp.isChildOf
import com.github.klee0kai.thekey.stone.ksp.ksp.isType
import com.github.klee0kai.thekey.stone.ksp.ksp.resolveAlias
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.*
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asClassName

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

val KSClassDeclaration.allParentDeclarations: Sequence<KSClassDeclaration>
    get() = (sequenceOf(this)
            + superTypes.mapNotNull { it.resolveAlias().declaration as? KSClassDeclaration })

val KSClassDeclaration.allIdentifierTypes: Sequence<KSType>
    get() = allParentDeclarations
        .flatMap { it.findComponentAnnotation() }
        .flatMap { it.identifiers }

fun List<KSValueParameter>.identifierParameters(
    allIdentifierTypes: List<KSType>,
) = filter { it.type.resolveAlias() in allIdentifierTypes }

fun List<KSValueParameter>.notIdentifierParameters(
    allIdentifierTypes: List<KSType>,
) = filter { it.type.resolveAlias() !in allIdentifierTypes }

fun List<KSValueParameter>.lifeCycleParameter() = firstOrNull {
    (it.type.resolveAlias().declaration as? KSClassDeclaration)
        ?.isChildOf(StoneLifeCycleOwner::class.asClassName()) == true
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
            standardScopeAnnotations.any { funAnnotation.annotationType.resolveAlias().declaration.isType(it) }
                    || funAnnotation.annotationType.resolveAlias().declaration.annotations.any { annotationOfAnnotation ->
                annotationOfAnnotation.annotationType.resolveAlias().declaration.isAnyType(
                    GcScopeAnnotation::class,
                    Scope::class,
                    javax.inject.Scope::class
                )
            }
        }
    }

val KSAnnotated.qualifierAnnotations: Sequence<KSAnnotation>
    get() {
        val standardQualifierAnnotations = listOf(
            Named::class,
            IgnoreQualifier::class,
        )

        return annotations.filter { funAnnotation ->
            standardQualifierAnnotations.any { funAnnotation.annotationType.resolveAlias().declaration.isType(it) }
                    || funAnnotation.annotationType.resolveAlias().declaration.annotations.any { annotationOfAnnotation ->
                annotationOfAnnotation.annotationType.resolveAlias().declaration.isType(Qualifier::class)
            }
        }
    }



