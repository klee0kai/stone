package com.github.klee0kai.thekey.stone.ksp.poet

import com.squareup.kotlinpoet.*

@DslMarker
annotation class TypeSpecDsl

@TypeSpecDsl
fun TypeSpec.Builder.genProperty(
    name: String,
    type: TypeName,
    vararg modifiers: KModifier,
    block: PropertySpec.Builder.() -> Unit = {}
): PropertySpec {
    return PropertySpec.builder(name, type, *modifiers)
        .apply(block)
        .build()
        .also {
            addProperty(it)
        }
}

@TypeSpecDsl
fun TypeSpec.Builder.genClass(
    className: ClassName,
    block: TypeSpec.Builder.() -> Unit = {},
) {
    addType(
        TypeSpec.classBuilder(className)
            .apply(block)
            .build()
    )
}

@TypeSpecDsl
fun TypeSpec.Builder.genObject(
    className: ClassName,
    block: TypeSpec.Builder.() -> Unit = {},
) {
    addType(
        TypeSpec.objectBuilder(className)
            .apply(block)
            .build()
    )
}

@TypeSpecDsl
fun TypeSpec.Builder.genInterface(
    className: ClassName,
    block: TypeSpec.Builder.() -> Unit = {},
) {
    addType(
        TypeSpec.interfaceBuilder(className)
            .apply(block)
            .build()
    )
}

@TypeSpecDsl
fun TypeSpec.Builder.genFun(
    name: String,
    block: FunSpec.Builder.() -> Unit = {},
) {
    addFunction(
        FunSpec.builder(name)
            .apply(block)
            .build()
    )
}

@TypeSpecDsl
fun TypeSpec.Builder.genPrimaryConstructor(
    block: FunSpec.Builder.() -> Unit = {},
) {
    primaryConstructor(
        FunSpec.constructorBuilder()
            .apply(block)
            .build()
    )
}

@TypeSpecDsl
fun TypeSpec.Builder.genConstructor(
    block: FunSpec.Builder.() -> Unit = {},
) {
    addFunction(
        FunSpec.constructorBuilder()
            .apply(block)
            .build()
    )
}