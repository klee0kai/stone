package com.github.klee0kai.thekey.stone.ksp.poet

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName

fun FunSpec.Builder.addAnnotation(
    ksAnnotation: KSAnnotation,
): FunSpec.Builder {
    val typeName = ksAnnotation.annotationType.resolve().toClassName()

    val annotationBuilder = AnnotationSpec.builder(typeName)

    for (arg in ksAnnotation.arguments) {
        val name = arg.name?.asString() ?: continue
        val value = arg.value

        // value может быть: primitive, enum, class, array
        annotationBuilder.addMember("%L = %L", name, formatAnnotationValue(value))
    }

    return this.addAnnotation(annotationBuilder.build())
}

fun formatAnnotationValue(
    value: Any?,
): String = when (value) {
    is String -> "\"${value}\""
    is KSAnnotation -> {
        val type = value.annotationType.resolve().toTypeName()
        "@$type"
    }

    is KSType -> value.toTypeName().toString() + "::class"
    is List<*> -> value.joinToString(", ", "{", "}") { formatAnnotationValue(it) }
    else -> value.toString()
}