package com.github.klee0kai.stone.helpers.wrap;

import com.github.klee0kai.stone._hidden_.types.StListUtils;
import com.github.klee0kai.stone._hidden_.types.StNl;
import com.github.klee0kai.stone.exceptions.StoneException;
import com.github.klee0kai.stone.helpers.codebuilder.SmartCode;
import com.github.klee0kai.stone.types.wrappers.AsyncProvide;
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

    public static boolean isNonCachingWrapper(TypeName typeName) {
        for (TypeName t : allParamTypes(typeName)) {
            WrapType wrapType = wrapTypes.get(rawTypeOf(t));
            if (wrapType != null && wrapType.isNoCachingWrapper)
                return true;
        }
        return false;
    }

    public static boolean isAsyncProvider(TypeName typeName) {
        for (TypeName t : allParamTypes(typeName)) {
            WrapType wrapType = wrapTypes.get(rawTypeOf(t));
            if (wrapType != null && wrapType.isAsyncProvider)
                return true;
        }
        return false;
    }

    public static boolean isList(TypeName typeName) {
        return StListUtils.indexOf(allParamTypes(typeName), (i, it) -> {
            WrapType wrapType = wrapTypes.get(rawTypeOf(it));
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

    public static TypeName listWrapTypeIfNeed(TypeName typeName) {
        if (isList(typeName))
            return ParameterizedTypeName.get(ClassName.get(List.class), nonWrappedType(typeName));
        return nonWrappedType(typeName);
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

        StListUtils.IFormat<TypeName, WrapType> wrapTypeFormat = it -> {
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

        LinkedList<WrapType> unwrapPath = new LinkedList<>(StListUtils.format(unwrapPathNames, wrapTypeFormat));
        LinkedList<WrapType> wrapPath = new LinkedList<>(StListUtils.format(wrapPathNames, wrapTypeFormat));

        while (!unwrapPath.isEmpty()) {
            WrapType unwrapType = unwrapPath.get(0);
            if (unwrapType.isList()) {
                int wrapListIndex = StListUtils.indexOf(wrapPath, (i, it) -> it.isList());
                if (wrapListIndex >= 0) {
                    TypeName unWrapItemType = paramType(unwrapPathNames.get(0));
                    TypeName wrapItemType = paramType(wrapPathNames.get(wrapListIndex));
                    WrapType wrapListType = wrapPath.get(wrapListIndex);
                    smartCode = wrapListType.inListFormat.formatCode(smartCode,
                            listItemCode -> transform(listItemCode.providingType(unWrapItemType), wrapItemType));

                    for (int i = 0; i <= wrapListIndex; i++) {
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
        for (Class cl : Arrays.asList(WeakReference.class, SoftReference.class, Reference.class)) {
            ClassName wrapper = ClassName.get(cl);
            ClassName creator = !Objects.equals(cl, Reference.class) ? wrapper : ClassName.get(WeakReference.class);

            WrapType wrapType = new WrapType();
            wrapType.isNoCachingWrapper = false;
            wrapType.typeName = wrapper;
            wrapType.wrap = (or) -> {
                SmartCode builder = SmartCode.builder()
                        .add(CodeBlock.of("$T.let(", StNl.class))
                        .add(or)
                        .add(CodeBlock.of(", $T::new )", creator));
                if (or.providingType != null)
                    builder.providingType(ParameterizedTypeName.get(wrapper, or.providingType));
                return builder;
            };

            wrapType.unwrap = (or) -> {
                SmartCode builder = SmartCode.builder()
                        .add(CodeBlock.of("$T.let( ", StNl.class))
                        .add(or)
                        .add(CodeBlock.of(", $T::get ) ", cl));
                if (or.providingType != null)
                    builder.providingType(paramType(or.providingType));
                return builder;
            };

            support(wrapType);
        }

        for (Class cl : Arrays.asList(PhantomProvide.class, Ref.class, Provider.class, LazyProvide.class, AsyncProvide.class)) {
            ClassName wrapper = ClassName.get(cl);

            WrapType wrapType = new WrapType();
            wrapType.isNoCachingWrapper = !Objects.equals(cl, LazyProvide.class) && !Objects.equals(cl, AsyncProvide.class);
            wrapType.isAsyncProvider = true;
            wrapType.typeName = wrapper;

            wrapType.wrap = (or) -> {
                // TODO sometimes work not well. Need use types directly
                SmartCode builder = SmartCode.builder()
                        .add(CodeBlock.of("new $T( () -> ", wrapType.isNoCachingWrapper ? ClassName.get(PhantomProvide.class) : wrapper))
                        .add(or)
                        .add(" )");
                if (or.providingType != null)
                    builder.providingType(ParameterizedTypeName.get(wrapper, or.providingType));
                return builder;
            };

            wrapType.unwrap = (or) -> {
                // TODO sometimes work not well. Need use types directly
                SmartCode builder = SmartCode.builder()
                        .add(CodeBlock.of("$T.let( ", StNl.class))
                        .add(or)
                        .add(CodeBlock.of(", $T::get ) ", cl));
                if (or.providingType != null)
                    builder.providingType(paramType(or.providingType));
                return builder;
            };
            support(wrapType);
        }

        int index = 0;
        for (Class cl : Arrays.asList(LinkedList.class, ArrayList.class, List.class, Collection.class)) {
            ClassName wrapper = ClassName.get(cl);
            boolean needConstructor = Arrays.asList(LinkedList.class, ArrayList.class).contains(cl);
            ClassName createType = index++ <= 0 ? wrapper : ClassName.get(ArrayList.class);

            WrapType wrapType = new WrapType();
            wrapType.typeName = wrapper;
            wrapType.wrap = (or) -> {
                SmartCode builder = SmartCode.builder();
                if (needConstructor)
                    builder.add(CodeBlock.of("$T.let( ", StNl.class));

                builder.add(CodeBlock.of("$T.list( ", StNl.class))
                        .add(or)
                        .add(")");

                if (needConstructor) builder.add(CodeBlock.of(", $T::new)", createType));
                if (or.providingType != null)
                    builder.providingType(ParameterizedTypeName.get(wrapper, or.providingType));
                return builder;
            };

            wrapType.unwrap = (or) -> {
                SmartCode builder = SmartCode.builder()
                        .add(CodeBlock.of("$T.first( ", StListUtils.class))
                        .add(or)
                        .add(CodeBlock.of(") "));
                if (or.providingType != null)
                    builder.providingType(paramType(or.providingType));
                return builder;
            };

            wrapType.inListFormat = (originalListCode, itemTransformFun) -> {
                SmartCode builder = SmartCode.builder();
                boolean isListNeedConstructor = needConstructor
                        || originalListCode.providingType != null && !Objects.equals(rawTypeOf(wrapper), rawTypeOf(originalListCode.providingType));

                if (isListNeedConstructor) builder.add(CodeBlock.of("$T.let( ", StNl.class));

                SmartCode itemTransform = itemTransformFun.formatCode(SmartCode.of("it", null));
                if (itemTransform.getSize() <= 1) {
                    //no transforms
                    builder.add(originalListCode);
                } else {
                    builder.add(CodeBlock.of("$T.format( ", StListUtils.class))
                            .add(originalListCode)
                            .add(", it ->  ")
                            .add(itemTransformFun.formatCode(SmartCode.of("it", null)))
                            .add(") ");

                }
                if (isListNeedConstructor) builder.add(CodeBlock.of(", $T::new)", createType));

                if (originalListCode.providingType != null)
                    builder.providingType(rawTypeOf(originalListCode.providingType));
                return builder;
            };
            support(wrapType);

        }
    }


}
