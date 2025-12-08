package com.github.klee0kai.thekey.stone.ksp.helpers.itemholder

import com.github.klee0kai.thekey.stone.ksp.helpers.isListType
import com.github.klee0kai.thekey.stone.ksp.helpers.noWrappedType
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.TypeSpec

interface ItemHolderCodeHelper {
    companion object;

    fun TypeSpec.Builder.genCacheField()

    fun codeGetCachedValue(): CodeBlock

    fun codeSetCachedValue(
        value: CodeBlock,
        onlyIfNull: Boolean,
    ): CodeBlock

    fun statementSwitchRef(
        paramsCode: CodeBlock,
    ): CodeBlock

    fun clearNullsStatement(): CodeBlock

}

fun ItemHolderCodeHelper.Companion.of(
    fieldName: String,
    returnType: KSType,
    idArguments: List<KSValueParameter>,
    cacheType: ItemCacheType,
): ItemHolderCodeHelper {
    val noWrappedReturnType = returnType
        .noWrappedType(
            wrappedTypes = idArguments.map { it.type.resolve() }
        )
    val defRefType = if (noWrappedReturnType.isListType()) {
        cacheType.toRefTypeList()
    } else {
        cacheType.toRefTypeSingle()
    }

    return when {
        idArguments.isEmpty() -> SingleItemHolderCodeHelper(
            fieldName = fieldName,
            returnType = returnType,
            nonWrappedReturnType = noWrappedReturnType,
            itemCacheType = cacheType,
            isListCaching = noWrappedReturnType.isListType(),
            defRefType = defRefType,
        )

        idArguments.size == 1 -> SimpleMapItemHolderCodeHelper(
            fieldName = fieldName,
            returnType = returnType,
            nonWrappedReturnType = noWrappedReturnType,
            itemCacheType = cacheType,
            isListCaching = noWrappedReturnType.isListType(),
            defRefType = defRefType,
            keyParam = idArguments.first(),
        )

        else -> MultiKeyMapItemHolderCodeHelper(
            fieldName = fieldName,
            returnType = returnType,
            nonWrappedReturnType = noWrappedReturnType,
            itemCacheType = cacheType,
            isListCaching = noWrappedReturnType.isListType(),
            defRefType = defRefType,
            keyArguments = idArguments,
        )
    }
}
