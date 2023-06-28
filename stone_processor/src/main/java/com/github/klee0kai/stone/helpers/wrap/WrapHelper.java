package com.github.klee0kai.stone.helpers.wrap;

import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.stone.closed.types.NullGet;
import com.github.klee0kai.stone.exceptions.StoneException;
import com.github.klee0kai.stone.helpers.codebuilder.SmartCode;
import com.github.klee0kai.stone.types.wrappers.LazyProvide;
import com.github.klee0kai.stone.types.wrappers.PhantomProvide;
import com.github.klee0kai.stone.types.wrappers.Ref;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import javax.inject.Provider;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.*;

import static com.github.klee0kai.stone.exceptions.ExceptionStringBuilder.createErrorMes;
import static com.github.klee0kai.stone.utils.ClassNameUtils.rawTypeOf;

public class WrapHelper {

    public static HashMap<TypeName, WrapType> wrapTypes = new HashMap<>();

    static {
        std();
    }

    public static void support(WrapType wrapType) {
        wrapTypes.putIfAbsent(wrapType.typeName, wrapType);
    }


    public static boolean isSupport(TypeName typeName) {
        return wrapTypes.containsKey(rawTypeOf(typeName));
    }

    public static boolean isGeneric(TypeName typeName) {
        WrapType wrapType = wrapTypes.get(typeName);
        return wrapType != null && wrapType.isGeneric;
    }


    public static TypeName paramType(TypeName typeName) {
        if (typeName instanceof ParameterizedTypeName) {
            ParameterizedTypeName par = (ParameterizedTypeName) typeName;
            if (isSupport(par.rawType) && par.typeArguments != null && !par.typeArguments.isEmpty())
                return par.typeArguments.get(0);
        }
        return typeName;
    }

    public static TypeName nonWrappedType(TypeName typeName) {
        if (typeName instanceof ParameterizedTypeName) {
            ParameterizedTypeName par = (ParameterizedTypeName) typeName;
            if (isSupport(par.rawType) && par.typeArguments != null && !par.typeArguments.isEmpty())
                return nonWrappedType(par.typeArguments.get(0));
        }
        return typeName;
    }

    public static List<TypeName> allParamTypes(TypeName typeName) {
        List<TypeName> allParams = new LinkedList<>();
        while (true) {
            allParams.add(typeName);
            ParameterizedTypeName paramType = typeName instanceof ParameterizedTypeName ? (ParameterizedTypeName) typeName : null;
            if (paramType == null || paramType.typeArguments.isEmpty()) break;
            typeName = paramType.typeArguments.get(0);
        }
        return allParams;
    }

    public static SmartCode transform(SmartCode code, TypeName wannaType) {
        if (code.providingType == null || Objects.equals(code.providingType, wannaType)) {
            return code;
        }

        SmartCode smartCode = SmartCode.builder().add(code);
        LinkedList<TypeName> wannaTypeParams = new LinkedList<>(allParamTypes(wannaType));
        LinkedList<TypeName> provideTypeParams = new LinkedList<>(allParamTypes(code.providingType));

        while (!ListUtils.endWith(wannaTypeParams, provideTypeParams)) {
            if (provideTypeParams.isEmpty() || !wrapTypes.containsKey(rawTypeOf(code.providingType))) {
                throw new StoneException(
                        createErrorMes()
                                .typeTransformNonSupport(code.providingType, wannaType)
                                .classNonFound(rawTypeOf(rawTypeOf(code.providingType)).toString())
                                .build(),
                        null
                );
            }
            smartCode = wrapTypes.get(rawTypeOf(code.providingType)).unwrap.apply(smartCode);
            provideTypeParams.pollFirst();
        }

        List<TypeName> wrappingTypes = wannaTypeParams.subList(0, wannaTypeParams.size() - provideTypeParams.size());
        Collections.reverse(wrappingTypes);

        Iterator<TypeName> wrIt = wrappingTypes.iterator();
        while (wrIt.hasNext()) {
            TypeName wr = wrIt.next();
            if (!wrapTypes.containsKey(rawTypeOf(wr))) {
                throw new StoneException(
                        createErrorMes()
                                .typeTransformNonSupport(code.providingType, wannaType)
                                .classNonFound(rawTypeOf(wr).toString())
                                .build(),
                        null
                );
            }

            smartCode = wrapTypes.get(rawTypeOf(wr)).wrap.apply(smartCode);
        }

        return smartCode;
    }

    private static void std() {
        for (Class cl : Arrays.asList(WeakReference.class, SoftReference.class)) {
            ClassName wrapper = ClassName.get(cl);

            WrapType wrapType = new WrapType();
            wrapType.isGeneric = false;
            wrapType.typeName = wrapper;
            wrapType.wrap = (or) -> {
                SmartCode builder = SmartCode.builder()
                        .add(CodeBlock.of("new $T( ", wrapper))
                        .add(or)
                        .add(" )");
                if (or.providingType != null)
                    builder.providingType(ParameterizedTypeName.get(wrapper, or.providingType));
                return builder;
            };

            wrapType.unwrap = (or) -> {
                SmartCode builder = SmartCode.builder()
                        .add(CodeBlock.of("$T.let( ", NullGet.class))
                        .add(or)
                        .add(CodeBlock.of(", $T::get ) ", Reference.class));
                if (or.providingType != null)
                    builder.providingType(paramType(or.providingType));
                return builder;
            };

            support(wrapType);
        }

        for (Class cl : Arrays.asList(PhantomProvide.class, Ref.class, Provider.class, LazyProvide.class)) {
            ClassName wrapper = ClassName.get(cl);

            WrapType wrapType = new WrapType();
            wrapType.isGeneric = !Objects.equals(cl, LazyProvide.class);
            wrapType.typeName = wrapper;

            wrapType.wrap = (or) -> {
                SmartCode builder = SmartCode.builder()
                        .add(CodeBlock.of("new $T( () -> ", wrapType.isGeneric ? ClassName.get(PhantomProvide.class) : wrapper))
                        .add(or)
                        .add(" )");
                if (or.providingType != null)
                    builder.providingType(ParameterizedTypeName.get(wrapper, or.providingType));
                return builder;
            };

            wrapType.unwrap = (or) -> {
                SmartCode builder = SmartCode.builder()
                        .add(CodeBlock.of("$T.let( ", NullGet.class))
                        .add(or)
                        .add(CodeBlock.of(", $T::get ) ", Ref.class));
                if (or.providingType != null)
                    builder.providingType(paramType(or.providingType));
                return builder;
            };
            support(wrapType);
        }
    }


}