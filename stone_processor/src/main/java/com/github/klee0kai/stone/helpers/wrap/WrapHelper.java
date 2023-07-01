package com.github.klee0kai.stone.helpers.wrap;

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
        LinkedList<TypeName> wrapPath = new LinkedList<>(allParamTypes(wannaType));
        LinkedList<TypeName> unwrapPath = new LinkedList<>(allParamTypes(code.providingType));
        Collections.reverse(wrapPath);
        while (!wrapPath.isEmpty() && !unwrapPath.isEmpty()
                && Objects.equals(rawTypeOf(unwrapPath.getLast()), rawTypeOf(wrapPath.getFirst()))) {
            unwrapPath.pollLast();
            wrapPath.pollFirst();
        }

        while (!unwrapPath.isEmpty()) {
            TypeName unWrapTypeName = rawTypeOf(unwrapPath.getFirst());
            WrapType unWrapType = wrapTypes.get(unWrapTypeName);
            if (unWrapType == null) {
                throw new StoneException(
                        createErrorMes()
                                .typeTransformNonSupport(code.providingType, wannaType)
                                .classNonFound(unWrapTypeName.toString())
                                .build(),
                        null
                );
            }
            if (unWrapType.isList()) {

            }
            smartCode = unWrapType.unwrap.formatCode(smartCode);
            unwrapPath.pollFirst();
        }

        while (!wrapPath.isEmpty()) {
            TypeName wrapTypeName = rawTypeOf(wrapPath.getFirst());
            WrapType wrapType = wrapTypes.get(wrapTypeName);
            if (wrapType == null) {
                throw new StoneException(
                        createErrorMes()
                                .typeTransformNonSupport(code.providingType, wannaType)
                                .classNonFound(wrapTypeName.toString())
                                .build(),
                        null
                );
            }
            smartCode = wrapType.wrap.formatCode(smartCode);
            wrapPath.pollFirst();
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

        for (Class cl : Arrays.asList(LinkedList.class, ArrayList.class, List.class, Collection.class, Iterable.class)) {
            ClassName wrapper = ClassName.get(cl);

            WrapType wrapType = new WrapType();

        }
    }


}
