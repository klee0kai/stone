package com.github.klee0kai.stone.helpers;

import com.github.klee0kai.stone.annotations.component.GcSoftScope;
import com.github.klee0kai.stone.annotations.component.GcStrongScope;
import com.github.klee0kai.stone.annotations.component.GcWeakScope;
import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.stone.closed.types.MultiKey;
import com.github.klee0kai.stone.closed.types.map.SoftMapItemHolder;
import com.github.klee0kai.stone.closed.types.map.StrongMapItemHolder;
import com.github.klee0kai.stone.closed.types.map.WeakMapItemHolder;
import com.github.klee0kai.stone.closed.types.single.SoftItemHolder;
import com.github.klee0kai.stone.closed.types.single.StrongItemHolder;
import com.github.klee0kai.stone.closed.types.single.WeakItemHolder;
import com.github.klee0kai.stone.model.FieldDetail;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.List;

public interface ItemHolderCodeHelper {

    enum ItemCacheType {
        Strong, Soft, Weak;


        public ClassName getGcScopeClassName() {
            switch (this) {
                case Weak:
                    return ClassName.get(GcWeakScope.class);
                case Strong:
                    return ClassName.get(GcStrongScope.class);
                case Soft:
                default:
                    return ClassName.get(GcSoftScope.class);
            }
        }
    }

    ClassName multiKeyClassName = ClassName.get(MultiKey.class);
    ClassName strongHolderClassName = ClassName.get(StrongItemHolder.class);
    ClassName softHolderClassName = ClassName.get(SoftItemHolder.class);
    ClassName weakHolderClassName = ClassName.get(WeakItemHolder.class);

    ClassName strongMapHolderClassName = ClassName.get(StrongMapItemHolder.class);
    ClassName softMapHolderClassName = ClassName.get(SoftMapItemHolder.class);
    ClassName weakMapHolderClassName = ClassName.get(WeakMapItemHolder.class);


    static ItemCacheType cacheTypeFrom(BindInstance.CacheType cacheType) {
        if (cacheType != null) switch (cacheType) {
            case Weak:
                return ItemCacheType.Weak;
            case Strong:
                return ItemCacheType.Strong;
            case Soft:
            default:
                return ItemCacheType.Soft;
        }
        return ItemCacheType.Soft;
    }

    static ItemCacheType cacheTypeFrom(Provide.CacheType cacheType) {
        if (cacheType != null) switch (cacheType) {
            case Weak:
                return ItemCacheType.Weak;
            case Strong:
                return ItemCacheType.Strong;
            case Soft:
            default:
                return ItemCacheType.Soft;
        }
        return ItemCacheType.Soft;
    }

    static ItemHolderCodeHelper of(
            String fieldName,
            TypeName fieldOrType,
            List<FieldDetail> qualifiers,
            ItemCacheType cacheType
    ) {
        if (qualifiers == null || qualifiers.isEmpty()) {
            SingleItemHolderHelper singleItemHolderHelper = new SingleItemHolderHelper();
            singleItemHolderHelper.fieldName = fieldName;
            singleItemHolderHelper.fieldType = fieldOrType;
            switch (cacheType) {
                case Strong:
                    singleItemHolderHelper.holderType = strongHolderClassName;
                    break;
                case Soft:
                    singleItemHolderHelper.holderType = softHolderClassName;
                    break;
                case Weak:
                    singleItemHolderHelper.holderType = weakHolderClassName;
                    break;
            }
            return singleItemHolderHelper;
        }

        if (qualifiers.size() == 1) {
            SimpleMapItemHolderHelper simpleMapItemHolderHelper = new SimpleMapItemHolderHelper();
            simpleMapItemHolderHelper.fieldName = fieldName;
            simpleMapItemHolderHelper.fieldType = fieldOrType;
            simpleMapItemHolderHelper.keyParam = qualifiers.get(0);
            switch (cacheType) {
                case Strong:
                    simpleMapItemHolderHelper.holderType = strongMapHolderClassName;
                    break;
                case Soft:
                    simpleMapItemHolderHelper.holderType = softMapHolderClassName;
                    break;
                case Weak:
                    simpleMapItemHolderHelper.holderType = weakMapHolderClassName;
                    break;
            }
            return simpleMapItemHolderHelper;
        }
        MultiKeyMapItemHolderHelper multiKeyMapItemHolderHelper = new MultiKeyMapItemHolderHelper();
        multiKeyMapItemHolderHelper.fieldName = fieldName;
        multiKeyMapItemHolderHelper.fieldType = fieldOrType;
        multiKeyMapItemHolderHelper.keyArgs = qualifiers;
        switch (cacheType) {
            case Strong:
                multiKeyMapItemHolderHelper.holderType = strongMapHolderClassName;
                break;
            case Soft:
                multiKeyMapItemHolderHelper.holderType = softMapHolderClassName;
                break;
            case Weak:
                multiKeyMapItemHolderHelper.holderType = weakMapHolderClassName;
                break;
        }
        return multiKeyMapItemHolderHelper;
    }

    FieldSpec.Builder cachedField();

    CodeBlock codeGetCachedValue();

    CodeBlock codeSetCachedValue(CodeBlock value);


    CodeBlock codeSetCachedIfNullValue(CodeBlock value);

    CodeBlock statementSwitchRef(CodeBlock paramsCode);


    class SingleItemHolderHelper implements ItemHolderCodeHelper {

        public String fieldName;
        public TypeName fieldType;
        public ClassName holderType;

        @Override
        public FieldSpec.Builder cachedField() {
            ParameterizedTypeName cacheType = ParameterizedTypeName.get(holderType, fieldType);
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
            return CodeBlock.of("$L.set( $L )", fieldName, value);
        }

        @Override
        public CodeBlock codeSetCachedIfNullValue(CodeBlock value) {
            return CodeBlock.of("$L.setIfNull( $L )", fieldName, value);
        }

        @Override
        public CodeBlock statementSwitchRef(CodeBlock paramsCode) {
            return CodeBlock.builder()
                    .addStatement("$L.switchCache($L)", fieldName, paramsCode)
                    .build();
        }

    }


    class SimpleMapItemHolderHelper implements ItemHolderCodeHelper {

        public String fieldName;
        public TypeName fieldType;
        public ClassName holderType;
        public FieldDetail keyParam;


        @Override
        public FieldSpec.Builder cachedField() {
            ParameterizedTypeName cacheType = ParameterizedTypeName.get(holderType, keyParam.type, fieldType);
            return FieldSpec.builder(cacheType, fieldName, Modifier.PRIVATE, Modifier.FINAL)
                    .initializer("new $T()", cacheType);
        }

        @Override
        public CodeBlock codeGetCachedValue() {
            return CodeBlock.builder()
                    .add("$L.get($L)", fieldName, keyParam.name)
                    .build();
        }

        @Override
        public CodeBlock codeSetCachedValue(CodeBlock value) {
            return CodeBlock.of(
                    "$L.set( $L , $L )",
                    fieldName, keyParam.name, value
            );
        }

        @Override
        public CodeBlock codeSetCachedIfNullValue(CodeBlock value) {
            return CodeBlock.of(
                    "$L.setIfNull( $L , $L )",
                    fieldName, keyParam.name, value
            );
        }

        @Override
        public CodeBlock statementSwitchRef(CodeBlock paramsCode) {
            return CodeBlock.builder()
                    .addStatement("$L.switchCache($L)", fieldName, paramsCode)
                    .build();
        }


    }


    class MultiKeyMapItemHolderHelper implements ItemHolderCodeHelper {

        public String fieldName;
        public TypeName fieldType;
        public ClassName holderType;

        public List<FieldDetail> keyArgs;


        @Override
        public FieldSpec.Builder cachedField() {
            ParameterizedTypeName cacheType = ParameterizedTypeName.get(holderType, multiKeyClassName, fieldType);
            return FieldSpec.builder(cacheType, fieldName, Modifier.PRIVATE, Modifier.FINAL)
                    .initializer("new $T()", cacheType);
        }

        @Override
        public CodeBlock codeGetCachedValue() {
            return CodeBlock.builder()
                    .add("$L.get(new $T($L) )", fieldName, multiKeyClassName,
                            String.join(",", ListUtils.format(keyArgs, (k) -> k.name)))
                    .build();
        }

        @Override
        public CodeBlock codeSetCachedValue(CodeBlock value) {
            return CodeBlock.of(
                    "$L.set( new $T( $L ), $L )", fieldName, multiKeyClassName,
                    String.join(",", ListUtils.format(keyArgs, (k) -> k.name)),
                    value
            );
        }

        @Override
        public CodeBlock codeSetCachedIfNullValue(CodeBlock value) {
            return CodeBlock.of(
                    "$L.setIfNull( new $T( $L ), $L )", fieldName, multiKeyClassName,
                    String.join(",", ListUtils.format(keyArgs, (k) -> k.name)),
                    value
            );
        }

        @Override
        public CodeBlock statementSwitchRef(CodeBlock paramsCode) {
            return CodeBlock.builder()
                    .addStatement("$L.switchCache($L)", fieldName, paramsCode)
                    .build();
        }

    }


}
