package com.github.klee0kai.thekey.stone.ksp.helpers.annotations

import com.github.klee0kai.stone.annotations.component.Component
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName

class ComponentAnnMirror(
    val identifiers: List<KSType>,
    val wrapperProviders: List<KSType>,
)

@Suppress("UNCHECKED_CAST")
fun KSAnnotated.findComponentAnnotation(
): Sequence<ComponentAnnMirror> = annotations
    .filter { it.annotationType.resolve().toClassName() == Component::class.asClassName() }
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
