package com.github.klee0kai.thekey.stone.ksp.helpers.annotations

import com.github.klee0kai.stone.annotations.component.*
import com.github.klee0kai.stone.annotations.dependencies.Dependencies
import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.stone.annotations.wrappers.WrappersCreator
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toTypeName

class ComponentAnnMirror(
    val identifiers: List<KSType>,
    val wrapperProviders: List<KSType>,
)

class WrapperCreatorAnnMirror(
    val wrappers: List<KSType>,
)

fun KSAnnotated.annotations(
    className: ClassName,
): Sequence<KSAnnotation> = annotations
    .filter { it.annotationType.resolve().toTypeName() == className }


fun KSAnnotated.anyAnnotation(
    vararg classNames: ClassName,
): Sequence<KSAnnotation> = annotations
    .filter { it.annotationType.resolve().toTypeName() in classNames }


fun KSAnnotated.stoneControlAnnotations(
): Sequence<KSAnnotation> = anyAnnotation(
    Component::class.asClassName(),
    ExtendOf::class.asClassName(),
    ModuleOriginFactory::class.asClassName(),
    ProtectInjected::class.asClassName(),
    RunGc::class.asClassName(),
    SwitchCache::class.asClassName(),
    Init::class.asClassName(),
    BindInstance::class.asClassName(),
    Provide::class.asClassName(),
    Module::class.asClassName(),
    Dependencies::class.asClassName(),
    WrappersCreator::class.asClassName(),
)


fun KSAnnotated.hasOnlyAnnotation(
    className: ClassName,
): Boolean {
    if (annotations.count() != 1) return false
    return annotations.first().annotationType.resolve().toTypeName() == className
}

fun KSAnnotated.hasOnlyStoneControlAnnotation(
    vararg classNames: ClassName,
): Boolean {
    if (stoneControlAnnotations().count() > classNames.size) return false
    return stoneControlAnnotations().all { it.annotationType.resolve().toTypeName() in classNames }
}


fun KSType.annotations(
    className: ClassName,
): Sequence<KSAnnotation> = annotations
    .filter { it.annotationType.resolve().toTypeName() == className }


@Suppress("UNCHECKED_CAST")
fun KSAnnotated.findComponentAnnotation(
): Sequence<ComponentAnnMirror> = annotations
    .filter { it.annotationType.resolve().toTypeName() == Component::class.asClassName() }
    .map { compAnn ->
        val identifiers = compAnn.arguments
            .firstOrNull { it.name?.asString() == "identifiers" }
            ?.value as? List<KSType>
            ?: emptyList()

        val wrapperProviders = compAnn.arguments
            .firstOrNull { it.name?.asString() == "wrapperProviders" }
            ?.value as? List<KSType>
            ?: emptyList()

        ComponentAnnMirror(
            identifiers = identifiers,
            wrapperProviders = wrapperProviders,
        )
    }

@Suppress("UNCHECKED_CAST")
fun KSAnnotated.findWrapperCreatorAnnotation(
): Sequence<WrapperCreatorAnnMirror> = annotations
    .filter { it.annotationType.resolve().toTypeName() == WrappersCreator::class.asClassName() }
    .map { compAnn ->
        val wrappers = compAnn.arguments
            .firstOrNull { it.name?.asString() == "wrappers" }
            ?.value as? List<KSType>
            ?: emptyList()

        WrapperCreatorAnnMirror(
            wrappers = wrappers,
        )
    }
