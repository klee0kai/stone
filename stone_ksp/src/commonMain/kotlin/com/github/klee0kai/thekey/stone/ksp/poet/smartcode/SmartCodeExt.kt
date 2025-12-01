package com.github.klee0kai.thekey.stone.ksp.poet.smartcode

import com.github.klee0kai.thekey.stone.ksp.poet.PoetDsl
import com.github.klee0kai.thekey.stone.ksp.property.map
import com.google.devtools.ksp.symbol.KSAnnotation
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.asTypeName

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

fun SmartCode.declareLocalVariable(
    variableName: String,
    qualifiers: List<KSAnnotation>,
    initVariable: SmartCode,
) = add {
    initVariable.providingType.map { type ->
        DeclareLocalVariable(
            variableName = variableName,
            type = type ?: Unit::class.asTypeName(),
            qualifierAnnotations = qualifiers,
        )
    }

    add("val %L = ", variableName)
    add(initVariable)
    add("\n")
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





