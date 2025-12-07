package com.github.klee0kai.thekey.stone.ksp.helpers.wrap

import com.github.klee0kai.thekey.stone.ksp.exceptions.ClassNotFoundStoneException
import com.github.klee0kai.thekey.stone.ksp.exceptions.ExceptionStringBuilder
import com.github.klee0kai.thekey.stone.ksp.exceptions.PrimitiveTypeNonSupportedStoneException
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.WildcardTypeName

object ClassNameUtils {

    /**
     * Get class name from full name.
     * Primitives and boxed types are not support
     *
     * @param clFullName class full name like "com.github.klee0kai.stone.annotations.component.class"
     * @return class name
     * @throws PrimitiveTypeNonSupportedStoneException primitive types are not support
     */
    fun classNameOf(clFullName: String): ClassName {
        var clFullName = clFullName
        try {
            if (clFullName.endsWith(".class")) clFullName = clFullName.take(clFullName.lastIndexOf(".class"))
            return ClassName.bestGuess(clFullName)
        } catch (e: Exception) {
            if (!clFullName.contains(".")) {
                throw PrimitiveTypeNonSupportedStoneException(
                    ExceptionStringBuilder.createErrorMes()
                        .primitiveTypesNonSupported(clFullName)
                        .build(),
                    e
                )
            }
            throw ClassNotFoundStoneException(
                ExceptionStringBuilder.createErrorMes()
                    .classNonFound(clFullName)
                    .build(),
                e
            )
        }
    }

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
        return typeName
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