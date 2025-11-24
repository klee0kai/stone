package com.github.klee0kai.thekey.stone.ksp.helpers.itemholder

import com.github.klee0kai.stone.__hidden__.types.MultiKey
import com.github.klee0kai.stone.__hidden__.types.holders.MapItemHolder
import com.github.klee0kai.stone.__hidden__.types.holders.StoneRefType
import com.github.klee0kai.thekey.stone.ksp.poet.genProperty
import com.github.klee0kai.thekey.stone.ksp.poet.smartcode.SmartCode
import com.github.klee0kai.thekey.stone.ksp.poet.smartcode.add
import com.github.klee0kai.thekey.stone.ksp.poet.smartcode.smartCode
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName

class MultiKeyMapItemHolderHelper(
    val fieldName: String,
    val returnType: KSType,
    val nonWrappedReturnType: KSType,
    val itemCacheType: ItemCacheType,
    val isListCaching: Boolean,
    val defRefType: StoneRefType,
    val keyArguments: List<KSValueParameter>,
) : ItemHolderHelper {

    override fun TypeSpec.Builder.genCacheField() {
        val cacheType = MapItemHolder::class.asClassName()
            .parameterizedBy(
                MultiKey::class.asClassName(),
                nonWrappedReturnType.toClassName(),
            )

        genProperty(fieldName, cacheType) {
            addModifiers(KModifier.PRIVATE)
            initializer("%T(%T.%L)", cacheType, StoneRefType::class, defRefType)
        }
    }


    override fun codeGetCachedValue(
    ): SmartCode = smartCode {
        val getMethod = if (isListCaching) "getList" else "get"
        add(
            "%L.%L(key = %T(%L))",
            fieldName, getMethod,
            MultiKey::class.asClassName(), keyArguments.joinToString(",") { it.name!!.asString() },
        )
        providingType.value = returnType.toClassName()
    }

    override fun codeSetCachedValue(
        value: CodeBlock,
        onlyIfNull: Boolean
    ): CodeBlock = smartCode {
        val setMethod = if (isListCaching) "setList" else "set"
        add(
            "%L.%L(key = %T(%L), onlyIfNull = %L ){ ",
            fieldName, setMethod,
            MultiKey::class.asClassName(), keyArguments.joinToString(",") { it.name!!.asString() },
            onlyIfNull
        )
        add(value)
        add("}")
        providingType.value = returnType.toClassName()
    }.collect()

    override fun statementSwitchRef(
        paramsCode: CodeBlock,
    ): CodeBlock = CodeBlock.builder()
        .addStatement("%L.switchCache(%L)", fieldName, paramsCode)
        .build()


    override fun clearNullsStatement(): CodeBlock = CodeBlock.Builder()
        .addStatement("%L.clearNulls()", fieldName)
        .build()


}