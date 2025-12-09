package com.github.klee0kai.thekey.stone.ksp.helpers.itemholder

import com.github.klee0kai.stone.__hidden__.types.MultiKey
import com.github.klee0kai.stone.__hidden__.types.holders.MapItemHolder
import com.github.klee0kai.stone.__hidden__.types.holders.StoneRefType
import com.github.klee0kai.thekey.stone.ksp.poet.codeBlock
import com.github.klee0kai.thekey.stone.ksp.poet.genProperty
import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

class MultiKeyMapItemHolderCodeHelper(
    val fieldName: String,
    val returnType: TypeName,
    val nonWrappedReturnType: TypeName,
    val itemCacheType: ItemCacheType,
    val isListCaching: Boolean,
    val defRefType: StoneRefType,
    val keyArguments: List<KSValueParameter>,
) : ItemHolderCodeHelper {

    override fun TypeSpec.Builder.genCacheField() {
        val cacheType = MapItemHolder::class.asClassName()
            .parameterizedBy(
                MultiKey::class.asClassName(),
                nonWrappedReturnType,
            )

        genProperty(fieldName, cacheType) {
            addModifiers(KModifier.PRIVATE)
            initializer("%T(%T.%L)", cacheType, StoneRefType::class, defRefType)
        }
    }


    override fun codeGetCachedValue(
    ) = codeBlock {
        val getMethod = if (isListCaching) "getList" else "get"
        add(
            "%L.%L(key = %T(%L))",
            fieldName, getMethod,
            MultiKey::class.asClassName(), keyArguments.joinToString(",") { it.name!!.asString() },
        )
    }

    override fun codeSetCachedValue(
        value: CodeBlock,
        onlyIfNull: Boolean
    ) = codeBlock {
        val setMethod = if (isListCaching) "setList" else "set"
        add(
            "%L.%L(key = %T(%L), onlyIfNull = %L ){ ",
            fieldName, setMethod,
            MultiKey::class.asClassName(), keyArguments.joinToString(",") { it.name!!.asString() },
            onlyIfNull
        )
        add(value)
        add("}")
    }

    override fun statementSwitchRef(
        paramsCode: CodeBlock,
    ): CodeBlock = CodeBlock.builder()
        .addStatement("%L.switchCache(%L)", fieldName, paramsCode)
        .build()


    override fun clearNullsStatement(): CodeBlock = CodeBlock.Builder()
        .addStatement("%L.clearNulls()", fieldName)
        .build()


}