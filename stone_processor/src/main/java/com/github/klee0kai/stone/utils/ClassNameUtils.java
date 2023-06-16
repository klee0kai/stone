package com.github.klee0kai.stone.utils;

import com.github.klee0kai.stone.exceptions.ClassNotFoundStoneException;
import com.github.klee0kai.stone.exceptions.PrimitiveTypeNonSupportedStoneException;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.WildcardTypeName;

import java.util.List;

import static com.github.klee0kai.stone.exceptions.ExceptionStringBuilder.createErrorMes;

/**
 * Class and type resolving utils.
 */
public class ClassNameUtils {

    /**
     * Get class name from full name.
     * Primitives and boxed types are not support
     *
     * @param clFullName class full name like "com.github.klee0kai.stone.annotations.component.class"
     * @return class name
     * @throws PrimitiveTypeNonSupportedStoneException primitive types are not support
     */
    public static ClassName classNameOf(String clFullName) {
        try {
            if (clFullName.endsWith(".class"))
                clFullName = clFullName.substring(0, clFullName.lastIndexOf(".class"));
            return ClassName.get(clFullName.substring(0, clFullName.lastIndexOf(".")),
                    clFullName.substring(clFullName.lastIndexOf(".") + 1));
        } catch (Exception e) {
            if (!clFullName.contains(".")) {
                throw new PrimitiveTypeNonSupportedStoneException(
                        createErrorMes()
                                .primitiveTypesNonSupported(clFullName)
                                .build(),
                        e);
            }
            throw new ClassNotFoundStoneException(
                    createErrorMes()
                            .classNonFound(clFullName)
                            .build(),
                    e);
        }

    }

    public static TypeName rawTypeOf(TypeName typeName) {
        if (typeName instanceof ParameterizedTypeName)
            return rawTypeOf(((ParameterizedTypeName) typeName).rawType);
        if (typeName instanceof WildcardTypeName) {
            List<TypeName> upperBounds = ((WildcardTypeName) typeName).upperBounds;
            return rawTypeOf(!upperBounds.isEmpty() ? upperBounds.get(0) : null);
        }
        return typeName;
    }

    public static String simpleName(TypeName typeName) {
        TypeName raw = rawTypeOf(typeName);
        if (raw instanceof ClassName)
            return ((ClassName) raw).simpleName();
        return null;
    }

}
