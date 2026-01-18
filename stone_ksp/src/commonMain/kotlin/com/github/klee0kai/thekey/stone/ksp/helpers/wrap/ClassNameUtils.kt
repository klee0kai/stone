package com.github.klee0kai.thekey.stone.ksp.helpers.wrap

import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.WildcardTypeName

object ClassNameUtils {

    fun rawTypeOf(typeName: TypeName): TypeName {
        if (typeName is ParameterizedTypeName) {
            return rawTypeOf((typeName).rawType)
        }
        if (typeName is WildcardTypeName) {
            val upperBounds = typeName.outTypes
            if (upperBounds.isNotEmpty()) {
                return rawTypeOf(upperBounds.first())
            }
        }
        return typeName.copy(nullable = false)
    }


    fun noWildCardType(type: TypeName): TypeName {
        if (type is WildcardTypeName) {
            val upperBounds = type.outTypes
            return if (!upperBounds.isEmpty()) {
                noWildCardType(upperBounds.first())
            } else {
                type
            }
        }
        return type
    }

}


fun TypeName.rawType(): TypeName = ClassNameUtils.rawTypeOf(this)