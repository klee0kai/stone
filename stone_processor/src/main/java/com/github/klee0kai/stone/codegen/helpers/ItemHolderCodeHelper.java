package com.github.klee0kai.stone.codegen.helpers;

import com.github.klee0kai.stone.annotations.BindInstance;
import com.github.klee0kai.stone.annotations.Provide;
import com.github.klee0kai.stone.holder.*;
import com.github.klee0kai.stone.model.ParamDetails;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.List;

public interface ItemHolderCodeHelper {

    enum ItemCacheType {
        STRONG, SOFT, WEAK
    }

    ClassName strongRefClassName = ClassName.get(StrongItemHolder.class);
    ClassName softRefClassName = ClassName.get(SoftItemHolder.class);
    ClassName weakRefClassName = ClassName.get(WeakItemHolder.class);

    ClassName strongMapClassName = ClassName.get(StrongMapItemHolder.class);
    ClassName softMapClassName = ClassName.get(SoftMapItemHolder.class);
    ClassName weakMapClassName = ClassName.get(WeakMapItemHolder.class);


    static ItemCacheType cacheTypeFrom(BindInstance.CacheType cacheType) {
        if (cacheType == null)
            return ItemCacheType.SOFT;
        switch (cacheType) {
            case WEAK:
                return ItemCacheType.WEAK;
            case SOFT:
                return ItemCacheType.SOFT;
            case STRONG:
                return ItemCacheType.STRONG;
            default:
                return ItemCacheType.SOFT;
        }
    }

    static ItemCacheType cacheTypeFrom(Provide.CacheType cacheType) {
        if (cacheType == null)
            return ItemCacheType.SOFT;
        switch (cacheType) {
            case WEAK:
                return ItemCacheType.WEAK;
            case SOFT:
                return ItemCacheType.SOFT;
            case STRONG:
                return ItemCacheType.STRONG;
            default:
                return ItemCacheType.SOFT;
        }
    }

    static ItemHolderCodeHelper of(String fieldName, TypeName fieldOrType, List<ParamDetails> qualifiers, ItemCacheType cacheType) {
        if (qualifiers == null || qualifiers.isEmpty()) {
            SingleItemHolderHelper singleItemHolderHelper = new SingleItemHolderHelper();
            singleItemHolderHelper.fieldName = fieldName;
            singleItemHolderHelper.fieldType = fieldOrType;
            switch (cacheType) {
                case STRONG:
                    singleItemHolderHelper.fieldHolderType = strongRefClassName;
                    singleItemHolderHelper.setWeakRefSupport = true;
                    break;
                case SOFT:
                    singleItemHolderHelper.fieldHolderType = softRefClassName;
                    singleItemHolderHelper.setWeakRefSupport = true;
                    break;
                case WEAK:
                    singleItemHolderHelper.fieldHolderType = weakRefClassName;
                    singleItemHolderHelper.setWeakRefSupport = false;
                    break;
            }
            return singleItemHolderHelper;
        }

        MapItemHolderHelper mapItemHolderHelper = new MapItemHolderHelper();

        return mapItemHolderHelper;
    }

    FieldSpec.Builder cachedField();

    /**
     * not close as Statement
     *
     * @return
     */
    CodeBlock codeGetCachedValue();

    /**
     * not close as Statement
     *
     * @return
     */
    CodeBlock codeSetCachedValue(CodeBlock value);


    CodeBlock codeToWeak();

    CodeBlock codeDefRef();

    boolean supportWeakRef();

    class SingleItemHolderHelper implements ItemHolderCodeHelper {

        public String fieldName;
        public TypeName fieldType;
        public ClassName fieldHolderType;

        public boolean setWeakRefSupport = false;

        @Override
        public FieldSpec.Builder cachedField() {
            ParameterizedTypeName cacheType = ParameterizedTypeName.get(fieldHolderType, fieldType);
            return FieldSpec.builder(cacheType, fieldName, Modifier.PRIVATE, Modifier.FINAL)
                    .initializer("new $T()", cacheType);
        }


        @Override
        public CodeBlock codeGetCachedValue() {
            return CodeBlock.builder()
                    .add("$L.get()", fieldName)
                    .build();
        }

        @Override
        public CodeBlock codeSetCachedValue(CodeBlock value) {
            return CodeBlock.builder()
                    .add("$L.set(", fieldName)
                    .add(value)
                    .add(")")
                    .build();
        }

        @Override
        public CodeBlock codeToWeak() {
            if (!setWeakRefSupport)
                return null;
            return CodeBlock.of("$L.weak()", fieldName);
        }

        @Override
        public CodeBlock codeDefRef() {
            if (!setWeakRefSupport)
                return null;
            return CodeBlock.of("$L.defRef()", fieldName);
        }

        @Override
        public boolean supportWeakRef() {
            return setWeakRefSupport;
        }
    }


    class MapItemHolderHelper implements ItemHolderCodeHelper {

        @Override
        public FieldSpec.Builder cachedField() {
            return null;
        }

        @Override
        public CodeBlock codeGetCachedValue() {
            return null;
        }

        @Override
        public CodeBlock codeSetCachedValue(CodeBlock value) {
            return null;
        }

        @Override
        public CodeBlock codeToWeak() {
            return null;
        }

        @Override
        public CodeBlock codeDefRef() {
            return null;
        }

        @Override
        public boolean supportWeakRef() {
            return false;
        }
    }

}
