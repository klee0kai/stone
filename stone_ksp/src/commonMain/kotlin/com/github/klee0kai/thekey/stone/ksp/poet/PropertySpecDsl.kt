package com.github.klee0kai.thekey.stone.ksp.poet

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec

@PoetDsl
fun PropertySpec.Builder.genGetter(
    block: FunSpec.Builder.() -> Unit = {}
): FunSpec {
    return FunSpec.getterBuilder()
        .apply(block)
        .build()
        .also {
            getter(it)
        }
}


@PoetDsl
fun PropertySpec.Builder.genSetter(
    block: FunSpec.Builder.() -> Unit = {}
): FunSpec {
    return FunSpec.setterBuilder()
        .apply(block)
        .build()
        .also {
            setter(it)
        }
}