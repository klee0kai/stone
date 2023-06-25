package com.github.klee0kai.stone.helpers.invokecall;

import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.stone.exceptions.StoneException;
import com.github.klee0kai.stone.helpers.codebuilder.SmartCode;
import com.github.klee0kai.stone.model.FieldDetail;
import com.github.klee0kai.stone.model.Pair;
import com.github.klee0kai.stone.types.wrappers.LazyProvide;
import com.github.klee0kai.stone.types.wrappers.PhantomProvide;
import com.github.klee0kai.stone.types.wrappers.Ref;
import com.github.klee0kai.stone.utils.ClassNameUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import javax.inject.Provider;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static com.github.klee0kai.stone.exceptions.ExceptionStringBuilder.createErrorMes;

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
                if (Objects.equals(type.rawType, ClassName.get(PhantomProvide.IProvide.class)))
                    return new Pair<>(orType, it.name + ".provide()");
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


    public static SmartCode transform(SmartCode code, TypeName wannaType) {
        if (code.providingType == null || Objects.equals(code.providingType, wannaType)) {
            return code;
        }
        boolean isUnwrap = Objects.equals(ClassNameUtils.argTypeOf(code.providingType), wannaType);
        boolean isWrap = wannaType instanceof ParameterizedTypeName
                && Objects.equals(ClassNameUtils.argTypeOf(wannaType), code.providingType);


        if (isWrap) return wrap(code, (ParameterizedTypeName) wannaType);
        if (isUnwrap) return unwrap(code, wannaType);

        throw new StoneException(
                createErrorMes()
                        .typeTransformNonSupport(code.providingType, wannaType)
                        .build(),
                null
        );
    }

    private static SmartCode wrap(SmartCode code, ParameterizedTypeName wannaType) {
        if (Objects.equals(ClassName.get(WeakReference.class), wannaType.rawType)
                || Objects.equals(ClassName.get(SoftReference.class), wannaType.rawType)) {

            return SmartCode.builder()
                    .add(CodeBlock.of("new $T(", wannaType))
                    .add(code)
                    .add(" )")
                    .providingType(wannaType);
        }

        ClassName phantomProvide = ClassName.get(PhantomProvide.class);
        if (Objects.equals(phantomProvide, wannaType.rawType)
                || Objects.equals(ClassName.get(Ref.class), wannaType.rawType)
                || Objects.equals(ClassName.get(Provider.class), wannaType.rawType)) {
            return SmartCode.builder()
                    .add(CodeBlock.of("new $T( () -> { return ", phantomProvide))
                    .add(code)
                    .add("; } )")
                    .providingType(wannaType);
        }

        if (Objects.equals(ClassName.get(LazyProvide.class), wannaType.rawType)) {
            return SmartCode.builder()
                    .add(CodeBlock.of("new $T( () -> { return ", wannaType))
                    .add(code)
                    .add("; } )")
                    .providingType(wannaType);
        }


        throw new StoneException(
                createErrorMes()
                        .typeTransformNonSupport(code.providingType, wannaType)
                        .build(),
                null
        );
    }


    private static SmartCode unwrap(SmartCode code, TypeName wannaType) {
        if (code.providingType instanceof ParameterizedTypeName) {
            ParameterizedTypeName type = (ParameterizedTypeName) code.providingType;
            if (Objects.equals(type.rawType, ClassName.get(PhantomProvide.IProvide.class)))
                return code.add(".provide()")
                        .providingType(wannaType);
            if (Objects.equals(type.rawType, ClassName.get(PhantomProvide.class)))
                return code.add(".get()")
                        .providingType(wannaType);
        }

        throw new StoneException(
                createErrorMes()
                        .typeTransformNonSupport(code.providingType, wannaType)
                        .build(),
                null
        );
    }


}
