package com.github.klee0kai.thekey.stone.ksp.helpers.itemholder

import com.github.klee0kai.stone.__hidden__.types.holders.SingleItemHolder
import com.github.klee0kai.stone.__hidden__.types.holders.StoneRefType
import com.github.klee0kai.thekey.stone.ksp.poet.codeBlock
import com.github.klee0kai.thekey.stone.ksp.poet.genProperty
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName

class SingleItemHolderCodeHelper(
    val fieldName: String,
    val returnType: KSType,
    val nonWrappedReturnType: KSType,
    val itemCacheType: ItemCacheType,
    val isListCaching: Boolean,
    val defRefType: StoneRefType,
) : ItemHolderCodeHelper {

    override fun TypeSpec.Builder.genCacheField() {
        val cacheType = SingleItemHolder::class.asClassName()
            .parameterizedBy(nonWrappedReturnType.toTypeName())

        genProperty(fieldName, cacheType) {
            addModifiers(KModifier.PRIVATE)
            initializer("%T(%T.%L)", cacheType, StoneRefType::class, defRefType)
        }
    }

    override fun codeGetCachedValue(
    ) = codeBlock {
        val getMethod = if (isListCaching) "getList" else "get"
        add("%L.%L()", fieldName, getMethod)
    }

    override fun codeSetCachedValue(
        value: CodeBlock,
        onlyIfNull: Boolean
    ) = codeBlock {
        val setMethod = if (isListCaching) "setList" else "set"
        add("%L.%L(onlyIfNull = %L ){ ", fieldName, setMethod, onlyIfNull)
        add(value)
        add(" }")
    }

    override fun statementSwitchRef(
        paramsCode: CodeBlock,
    ): CodeBlock = CodeBlock.builder()
        .addStatement("%L.switchCache(%L)", fieldName, paramsCode)
        .build()

    override fun clearNullsStatement(): CodeBlock = CodeBlock.of("")

}