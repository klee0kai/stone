package com.github.klee0kai.stone.helpers.wrap;

import com.github.klee0kai.stone._hidden_.types.ListUtils;
import com.github.klee0kai.stone._hidden_.types.NullGet;
import com.github.klee0kai.stone.exceptions.StoneException;
import com.github.klee0kai.stone.wrappers.AsyncProvide;
import com.github.klee0kai.stone.wrappers.LazyProvide;
import com.github.klee0kai.stone.wrappers.PhantomProvide;
import com.github.klee0kai.stone.wrappers.Ref;
import com.squareup.javapoet.*;

import javax.inject.Provider;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.*;

import static com.github.klee0kai.stone.exceptions.ExceptionStringBuilder.createErrorMes;
import static com.github.klee0kai.stone.utils.ClassNameUtils.noWildCardType;
import static com.github.klee0kai.stone.utils.ClassNameUtils.rawTypeOf;

public class WrapHelper {

    HashMap<TypeName, WrapType> wrapTypes = new HashMap<>();

    public void reInit() {
        wrapTypes.clear();
        std();
    }

    public void support(WrapType wrapType) {
        wrapTypes.putIfAbsent(wrapType.typeName, wrapType);
    }


    public boolean isSupport(TypeName typeName) {
        return wrapTypes.containsKey(rawTypeOf(typeName));
    }

    public boolean isNonCachingWrapper(TypeName typeName) {
        for (TypeName t : allParamTypes(typeName)) {
            WrapType wrapType = wrapTypes.get(rawTypeOf(t));
            if (wrapType != null && wrapType.isNoCachingWrapper)
                return true;
        }
        return false;
    }

    public boolean isAsyncProvider(TypeName typeName) {
        for (TypeName t : allParamTypes(typeName)) {
            WrapType wrapType = wrapTypes.get(rawTypeOf(t));
            if (wrapType != null && wrapType.isAsyncProvider)
                return true;
        }
        return false;
    }

    public boolean isList(TypeName typeName) {
        return ListUtils.indexOf(allParamTypes(typeName), (i, it) -> {
            WrapType wrapType = wrapTypes.get(rawTypeOf(it));
            return wrapType != null && wrapType.isList();
        }) >= 0;
    }


    public TypeName paramType(TypeName typeName) {
        if (typeName instanceof ParameterizedTypeName) {
            ParameterizedTypeName par = (ParameterizedTypeName) typeName;
            if (isSupport(par.rawType) && par.typeArguments != null && !par.typeArguments.isEmpty())
                return par.typeArguments.get(0);
        }
        return typeName;
    }

    /**
     * com.github.klee0kai.stone.wrappers.LazyProvide<com.github.klee0kai.test.tech.components.Battery> -> com.github.klee0kai.test.tech.components.Battery
     * ? extends java.lang.ref.WeakReference<com.github.klee0kai.test.car.model.Window> -> com.github.klee0kai.test.car.model.Window
     */
    public TypeName nonWrappedType(TypeName typeName) {
        if (typeName instanceof ParameterizedTypeName) {
            ParameterizedTypeName par = (ParameterizedTypeName) typeName;
            if (isSupport(par.rawType) && par.typeArguments != null && !par.typeArguments.isEmpty())
                return nonWrappedType(par.typeArguments.get(0));
        }
        if (typeName instanceof WildcardTypeName) {
            WildcardTypeName par = (WildcardTypeName) typeName;
            if (!par.upperBounds.isEmpty())
                return nonWrappedType(par.upperBounds.get(0));
        }
        return typeName;
    }

    /**
     * java.util.List<com.github.klee0kai.stone.wrappers.Ref<com.github.klee0kai.test.boxed.model.CarBox<com.github.klee0kai.test.car.model.Window>>> ->
     * java.util.List<com.github.klee0kai.test.boxed.model.CarBox<com.github.klee0kai.test.car.model.Window>>
     */
    public TypeName listWrapTypeIfNeed(TypeName typeName) {
        if (isList(typeName))
            return ParameterizedTypeName.get(ClassName.get(List.class), nonWrappedType(typeName));
        return nonWrappedType(typeName);
    }

    public List<TypeName> allParamTypes(TypeName typeName) {
        typeName = noWildCardType(typeName);
        List<TypeName> allParams = new LinkedList<>();
        while (true) {
            allParams.add(typeName);
            ParameterizedTypeName paramType = typeName instanceof ParameterizedTypeName ? (ParameterizedTypeName) typeName : null;
            if (paramType == null || paramType.typeArguments.isEmpty()) break;
            typeName = noWildCardType(paramType.typeArguments.get(0));
        }
        return allParams;
    }


    public CodeBlock transform(
            TypeName providingType,
            TypeName wannaType,
            CodeBlock code

    ) {
        if (providingType == null || Objects.equals(providingType, wannaType)) {
            return code;
        }

        CodeBlock.Builder codeBuilder = CodeBlock.builder().add(code);
        LinkedList<TypeName> wrapPathNames = new LinkedList<>(allParamTypes(wannaType));
        LinkedList<TypeName> unwrapPathNames = new LinkedList<>(allParamTypes(providingType));
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
                                .typeTransformNonSupport(rawTypeOf(providingType), wannaType)
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

                    codeBuilder = wrapListType.inListFormat.formatCode(
                            unwrapType.typeName,
                            codeBuilder.build(),
                            (listItemCode) ->
                                    transform(
                                            unWrapItemType,
                                            wrapItemType,
                                            listItemCode
                                    )
                    ).toBuilder();

                    for (int i = 0; i <= wrapListIndex; i++) {
                        wrapPath.pollFirst();
                        wrapPathNames.pollFirst();
                    }
                    unwrapPath.clear();
                    unwrapPathNames.clear();
                    break;
                }
            }
            codeBuilder = unwrapType.unwrap.formatCode(codeBuilder.build())
                    .toBuilder();

            unwrapPath.pollFirst();
            unwrapPathNames.pollFirst();
        }

        while (!wrapPath.isEmpty()) {
            codeBuilder = wrapPath.get(0).wrap.formatCode(codeBuilder.build())
                    .toBuilder();

            wrapPath.pollFirst();
            wrapPathNames.pollFirst();
        }

        return codeBuilder.build();
    }

    private void std() {
        for (Class cl : Arrays.asList(WeakReference.class, SoftReference.class, Reference.class)) {
            ClassName wrapper = ClassName.get(cl);
            ClassName creator = !Objects.equals(cl, Reference.class) ? wrapper : ClassName.get(WeakReference.class);

            WrapType wrapType = new WrapType();
            wrapType.isNoCachingWrapper = false;
            wrapType.typeName = wrapper;
            wrapType.wrap = (or) ->
                    CodeBlock.builder()
                            .add("$T.let(", NullGet.class)
                            .add(or)
                            .add(", $T::new )", creator)
                            .build();

            wrapType.unwrap = (or) ->
                    CodeBlock.builder()
                            .add("$T.let( ", NullGet.class)
                            .add(or)
                            .add(", $T::get ) ", cl)
                            .build();

            support(wrapType);
        }

        for (Class cl : Arrays.asList(PhantomProvide.class, Ref.class, Provider.class, LazyProvide.class, AsyncProvide.class)) {
            ClassName wrapper = ClassName.get(cl);

            WrapType wrapType = new WrapType();
            wrapType.isNoCachingWrapper = !Objects.equals(cl, LazyProvide.class) && !Objects.equals(cl, AsyncProvide.class);
            wrapType.isAsyncProvider = true;
            wrapType.typeName = wrapper;

            wrapType.wrap = (or) ->
                    CodeBlock.builder()
                            .add("new $T( () -> ", wrapType.isNoCachingWrapper ? ClassName.get(PhantomProvide.class) : wrapper)
                            .add(or)
                            .add(" )")
                            .build();

            wrapType.unwrap = (or) ->
                    CodeBlock.builder()
                            .add("$T.let( ", NullGet.class)
                            .add(or)
                            .add(", $T::get ) ", cl)
                            .build();
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
                CodeBlock.Builder builder = CodeBlock.builder();
                if (needConstructor) builder.add("$T.let( ", NullGet.class);
                builder.add("$T.list( $L ) ", NullGet.class, or);
                if (needConstructor) builder.add(", $T::new)", createType);
                return builder.build();
            };

            wrapType.unwrap = (or) -> CodeBlock.builder()
                    .add("$T.first( $L )", ListUtils.class, or)
                    .build();

            wrapType.inListFormat = (originalListType, originalListCode, itemTransformFun) -> {
                CodeBlock.Builder builder = CodeBlock.builder();
                boolean isListNeedConstructor = needConstructor
                        || originalListType != null && !Objects.equals(rawTypeOf(wrapper), rawTypeOf(originalListType));

                if (isListNeedConstructor) builder.add("$T.let( ", NullGet.class);

                CodeBlock itemTransform = itemTransformFun.formatCode(CodeBlock.of("it"));
                if (Objects.equals(itemTransform.toString(), "it")) {
                    //no transforms
                    builder.add(originalListCode);
                } else {
                    builder.add("$T.format( ", ListUtils.class)
                            .add(originalListCode)
                            .add(", it ->  ")
                            .add(itemTransformFun.formatCode(CodeBlock.of("it")))
                            .add(") ");

                }
                if (isListNeedConstructor) builder.add(", $T::new)", createType);

                return builder.build();
            };
            support(wrapType);

        }
    }


}
