package com.github.klee0kai.thekey.stone.ksp.helpers.itemholder

import com.github.klee0kai.stone.__hidden__.types.holders.SingleItemHolder
import com.github.klee0kai.stone.__hidden__.types.holders.StoneRefType
import com.github.klee0kai.thekey.stone.ksp.poet.genProperty
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName

class SingleItemHolderHelper(
    val fieldName: String,
    val returnType: KSType,
    val nonWrappedReturnType: KSType,
    val itemCacheType: ItemCacheType,
    val isListCaching: Boolean,
    val defRefType: StoneRefType,
) : ItemHolderHelper {

    override fun TypeSpec.Builder.genCacheField() {
        val cacheType = SingleItemHolder::class.asClassName()
            .parameterizedBy(nonWrappedReturnType.toClassName())

        genProperty(fieldName, cacheType) {
            addModifiers(KModifier.PRIVATE)
            initializer("%T(%T.%L)", cacheType, StoneRefType::class, defRefType)
        }
    }

}