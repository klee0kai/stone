package com.github.klee0kai.thekey.stone.ksp.poet

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec

@PoetDsl
fun CodeBlock.Builder.controlFlow(
    controlFlow: String,
    vararg args: Any,
    block: CodeBlock.Builder.() -> Unit,
) = apply {
    beginControlFlow(controlFlow, *args)
        .add(CodeBlock.builder().apply(block).build())
        .endControlFlow()
}

@PoetDsl
fun FunSpec.Builder.controlFlow(
    controlFlow: String,
    vararg args: Any,
    block: CodeBlock.Builder.() -> Unit,
) = apply {
    beginControlFlow(controlFlow, *args)
        .addCode(CodeBlock.builder().apply(block).build())
        .endControlFlow()
}