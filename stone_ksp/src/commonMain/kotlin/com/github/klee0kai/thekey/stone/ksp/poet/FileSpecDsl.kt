package com.github.klee0kai.thekey.stone.ksp.poet

import com.squareup.kotlinpoet.*

@PoetDsl
fun genFileSpec(
    packageName: String,
    fileName: String,
    block: FileSpec.Builder.() -> Unit,
): FileSpec {
    return FileSpec.builder(packageName, fileName)
        .also(block)
        .build()
}

@PoetDsl
fun FileSpec.Builder.genProperty(
    name: String,
    type: TypeName,
    vararg modifiers: KModifier,
    block: PropertySpec.Builder.() -> Unit = {}
) {
    addProperty(
        PropertySpec.builder(name, type, *modifiers)
            .apply(block)
            .build()
    )
}

@PoetDsl
fun FileSpec.Builder.genClass(
    className: ClassName,
    block: TypeSpec.Builder.() -> Unit = {},
) {
    addType(
        TypeSpec.classBuilder(className)
            .apply(block)
            .build()
    )
}


@PoetDsl
fun FileSpec.Builder.genObject(
    className: ClassName,
    block: TypeSpec.Builder.() -> Unit = {},
) {
    addType(
        TypeSpec.objectBuilder(className)
            .apply(block)
            .build()
    )
}

@PoetDsl
fun FileSpec.Builder.genInterface(
    className: ClassName,
    block: TypeSpec.Builder.() -> Unit = {},
) {
    addType(
        TypeSpec.interfaceBuilder(className)
            .apply(block)
            .build()
    )
}

@PoetDsl
fun FileSpec.Builder.genFun(
    name: String,
    block: FunSpec.Builder.() -> Unit = {},
) {
    addFunction(
        FunSpec.builder(name)
            .apply(block)
            .build()
    )
}


