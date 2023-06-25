package com.github.klee0kai.stone.helpers.invokecall;

import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.stone.model.FieldDetail;
import com.github.klee0kai.stone.model.Pair;
import com.github.klee0kai.stone.types.wrappers.PhantomProvide;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class GenArgumentFunctions {


    /**
     * Simple unwrap argument from wrapped type (WeakReference and other).
     * If you have arguments like <br>
     * {@code Provide<SomeClass> someVariable;}  <br>
     * unwrapArgument support to unwrap SomeClass type on invoke method, like <br>
     * {@code doSmth(someVariable.provide()) }
     *
     * @param envFields available fields
     * @return unwrapped field get code, or null
     */
    public static Function<TypeName, CodeBlock> unwrapArgument(List<FieldDetail> envFields) {
        List<Pair<TypeName, String>> provideFields = ListUtils.format(envFields, it -> {
            if (it.type instanceof ParameterizedTypeName) {
                ParameterizedTypeName type = (ParameterizedTypeName) it.type;
                TypeName orType = type.typeArguments.get(type.typeArguments.size() - 1);
                if (Objects.equals(type.rawType, ClassName.get(PhantomProvide.class)))
                    return new Pair<>(orType, it.name + ".get()");
            }
            return new Pair<>(it.type, it.name);
        });

        return wannaType -> {
            for (Pair<TypeName, String> provideField : provideFields) {
                if (Objects.equals(wannaType, provideField.first))
                    return CodeBlock.of(provideField.second);
            }
            return null;
        };
    }





}
