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

    public static boolean isList(TypeName typeName) {
        return ListUtils.indexOf(allParamTypes(typeName), (i, it) -> {
            WrapType wrapType = wrapTypes.get(rawTypeOf(typeName));
            return wrapType != null && wrapType.isList();
        }) >= 0;
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
        LinkedList<TypeName> wrapPathNames = new LinkedList<>(allParamTypes(wannaType));
        LinkedList<TypeName> unwrapPathNames = new LinkedList<>(allParamTypes(code.providingType));
        Collections.reverse(wrapPathNames);
        while (!wrapPathNames.isEmpty() && !unwrapPathNames.isEmpty()
                && Objects.equals(rawTypeOf(unwrapPathNames.getLast()), rawTypeOf(wrapPathNames.getFirst()))) {
            unwrapPathNames.pollLast();
            wrapPathNames.pollFirst();
        }

        ListUtils.IFormat<TypeName, WrapType> wrapTypeFormat = it -> {
            WrapType type = wrapTypes.get(rawTypeOf(it));
            if (type == null) {
                throw new StoneException(
                        createErrorMes()
                                .typeTransformNonSupport(code.providingType, wannaType)
                                .classNonFound(it.toString())
                                .build(),
                        null
                );
            }
            return type;
        };

        LinkedList<WrapType> unwrapPath = new LinkedList<>(ListUtils.format(unwrapPathNames, wrapTypeFormat));
        LinkedList<WrapType> wrapPath = new LinkedList<>(ListUtils.format(wrapPathNames, wrapTypeFormat));

        while (!unwrapPath.isEmpty()) {
            WrapType unwrapType = unwrapPath.get(0);
            if (unwrapType.isList()) {
                int wrapListIndex = ListUtils.indexOf(wrapPath, (i, it) -> it.isList());
                if (wrapListIndex >= 0) {
                    TypeName unWrapItemType = paramType(unwrapPathNames.get(0));
                    TypeName wrapItemType = paramType(wrapPathNames.get(wrapListIndex));
                    WrapType wrapListType = wrapPath.get(wrapListIndex);
                    smartCode = wrapListType.inListFormat.formatCode(smartCode,
                            listItemCode -> transform(listItemCode.providingType(unWrapItemType), wrapItemType));

                    for (int i = 0; i <= wrapListIndex; i++){
                        wrapPath.pollFirst();
                        wrapPathNames.pollFirst();
                    }
                    unwrapPath.clear();
                    unwrapPathNames.clear();
                    break;
                }
            }
            smartCode = unwrapType.unwrap.formatCode(smartCode);
            unwrapPath.pollFirst();
            unwrapPathNames.pollFirst();
        }

        while (!wrapPath.isEmpty()) {
            smartCode = wrapPath.get(0).wrap.formatCode(smartCode);
            wrapPath.pollFirst();
            wrapPathNames.pollFirst();
        }

        return smartCode
                .providingType(wannaType);
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
                        .add(CodeBlock.of(", $T::get ) ", cl));
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
                        .add(CodeBlock.of(", $T::get ) ", cl));
                if (or.providingType != null)
                    builder.providingType(paramType(or.providingType));
                return builder;
            };
            support(wrapType);
        }

        int index = 0;
        for (Class cl : Arrays.asList(LinkedList.class, ArrayList.class, List.class, Collection.class, Iterable.class)) {
            ClassName wrapper = ClassName.get(cl);
            boolean needConstructor = Arrays.asList(LinkedList.class, ArrayList.class).contains(cl);
            ClassName createType = index++ <= 0 ? wrapper : ClassName.get(ArrayList.class);

            WrapType wrapType = new WrapType();
            wrapType.typeName = wrapper;
            wrapType.wrap = (or) -> {
                SmartCode builder = SmartCode.builder();
                if (needConstructor)
                    builder.add(CodeBlock.of("new $T( ", createType));

                builder.add(CodeBlock.of("$T.asList( ", Arrays.class))
                        .add(or)
                        .add(")");

                if (needConstructor) builder.add(")");
                if (or.providingType != null)
                    builder.providingType(ParameterizedTypeName.get(wrapper, or.providingType));
                return builder;
            };

            wrapType.unwrap = (or) -> {
                SmartCode builder = SmartCode.builder()
                        .add(CodeBlock.of("$T.first( ", ListUtils.class))
                        .add(or)
                        .add(CodeBlock.of(") "));
                if (or.providingType != null)
                    builder.providingType(paramType(or.providingType));
                return builder;
            };

            wrapType.inListFormat = (originalListCode, itemTransformFun) -> {
                SmartCode builder = SmartCode.builder();
                if (needConstructor) builder.add(CodeBlock.of("new $T( ", createType));

                builder.add(CodeBlock.of("$T.format( ", ListUtils.class))
                        .add(originalListCode)
                        .add(",  it ->  ")
                        .add(itemTransformFun.formatCode(
                                SmartCode.of("it", null)
                        ))
                        .add(") ");

                if (needConstructor) builder.add(")");

                if (originalListCode.providingType != null)
                    builder.providingType(rawTypeOf(originalListCode.providingType));
                return builder;
            };
            support(wrapType);

        }
    }


}
