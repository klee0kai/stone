package com.github.klee0kai.thekey.stone.ksp.poet

import com.github.klee0kai.thekey.stone.ksp.target.declareSameParameters
import com.github.klee0kai.thekey.stone.ksp.target.isSuspend
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.toClassName

@PoetDsl
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

@PoetDsl
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

@PoetDsl
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

@PoetDsl
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

@PoetDsl
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

@PoetDsl
fun TypeSpec.Builder.genOverrideFun(
    func: KSFunctionDeclaration,
    block: FunSpec.Builder.() -> Unit = {},
) {
    genFun(func.simpleName.asString()) {
        addModifiers(KModifier.OVERRIDE)
        if (func.isSuspend) addModifiers(KModifier.SUSPEND)
        declareSameParameters(func)
        func.returnType?.resolve()?.toClassName()?.let { returns(it) }

        block()
    }
}

@PoetDsl
fun TypeSpec.Builder.genPrimaryConstructor(
    block: FunSpec.Builder.() -> Unit = {},
) {
    primaryConstructor(
        FunSpec.constructorBuilder()
            .apply(block)
            .build()
    )
}

@PoetDsl
fun TypeSpec.Builder.genConstructor(
    block: FunSpec.Builder.() -> Unit = {},
) {
    addFunction(
        FunSpec.constructorBuilder()
            .apply(block)
            .build()
    )
}