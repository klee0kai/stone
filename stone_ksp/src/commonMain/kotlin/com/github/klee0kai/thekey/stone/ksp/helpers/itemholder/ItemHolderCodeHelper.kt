package com.github.klee0kai.thekey.stone.ksp.helpers.itemholder

import com.github.klee0kai.thekey.stone.ksp.helpers.wrap.WrapHelper
import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec

interface ItemHolderCodeHelper {
    companion object;

    val fieldName: String

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
    returnType: TypeName,
    idArguments: List<KSValueParameter>,
    cacheType: ItemCacheType,
    wrapHelper: WrapHelper,
): ItemHolderCodeHelper {

    return when {
        idArguments.isEmpty() -> SingleItemHolderCodeHelper(
            fieldName = fieldName,
            returnType = returnType,
            nonWrappedReturnType = wrapHelper.nonWrappedType(returnType),
            itemCacheType = cacheType,
            isListCaching = wrapHelper.isList(returnType),
            defRefType = if (wrapHelper.isList(returnType)) cacheType.toRefTypeList() else cacheType.toRefTypeSingle(),
        )

        idArguments.size == 1 -> SimpleMapItemHolderCodeHelper(
            fieldName = fieldName,
            returnType = returnType,
            nonWrappedReturnType = wrapHelper.nonWrappedType(returnType),
            itemCacheType = cacheType,
            isListCaching = wrapHelper.isList(returnType),
            defRefType = if (wrapHelper.isList(returnType)) cacheType.toRefTypeList() else cacheType.toRefTypeSingle(),
            keyParam = idArguments.first(),
        )

        else -> MultiKeyMapItemHolderCodeHelper(
            fieldName = fieldName,
            returnType = returnType,
            nonWrappedReturnType = wrapHelper.nonWrappedType(returnType),
            itemCacheType = cacheType,
            isListCaching = wrapHelper.isList(returnType),
            defRefType = if (wrapHelper.isList(returnType)) cacheType.toRefTypeList() else cacheType.toRefTypeSingle(),
            keyArguments = idArguments,
        )
    }
}
