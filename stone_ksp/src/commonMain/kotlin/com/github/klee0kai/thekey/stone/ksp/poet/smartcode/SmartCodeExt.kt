package com.github.klee0kai.thekey.stone.ksp.poet.smartcode

import com.github.klee0kai.thekey.stone.ksp.poet.PoetDsl
import com.squareup.kotlinpoet.CodeBlock

fun SmartCode.add(
    code: String,
) {
    add(CodeBlock.of(code))
}

fun SmartCode.add(
    code: String,
    vararg arg: Any
) {
    add(CodeBlock.of(code, *arg))
}


@PoetDsl
fun smartCode(
    block: SmartCode.() -> Unit,
) = SmartCode()
    .apply(block)


@PoetDsl
fun smartCode(
    string: String,
) = SmartCode()
    .apply {
        add(string)
    }


@PoetDsl
fun SmartCode.add(
    block: SmartCode.() -> Unit,
) {
    val parentBlock = this
    val builder = SmartCode(parentBlock)
    availableVariables.source = parentBlock.availableVariables
    builder.block()
    parentBlock.add(builder)
}





