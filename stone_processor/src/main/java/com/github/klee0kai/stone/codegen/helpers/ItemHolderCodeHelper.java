package com.github.klee0kai.stone.codegen.helpers;

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

    ClassName multiKeyMapClassName = ClassName.get(MultiKey.class);
    ClassName strongRefClassName = ClassName.get(StrongItemHolder.class);
    ClassName softRefClassName = ClassName.get(SoftItemHolder.class);
    ClassName weakRefClassName = ClassName.get(WeakItemHolder.class);

    ClassName strongMapClassName = ClassName.get(StrongMapItemHolder.class);
    ClassName softMapClassName = ClassName.get(SoftMapItemHolder.class);
    ClassName weakMapClassName = ClassName.get(WeakMapItemHolder.class);


    static ItemCacheType cacheTypeFrom(BindInstance.CacheType cacheType) {
        if (cacheType == null)
            return ItemCacheType.Soft;
        switch (cacheType) {
            case Weak:
                return ItemCacheType.Weak;
            case Soft:
                return ItemCacheType.Soft;
            case Strong:
                return ItemCacheType.Strong;
            default:
                return ItemCacheType.Soft;
        }
    }

    static ItemCacheType cacheTypeFrom(Provide.CacheType cacheType) {
        if (cacheType == null)
            return ItemCacheType.Soft;
        switch (cacheType) {
            case Weak:
                return ItemCacheType.Weak;
            case Soft:
                return ItemCacheType.Soft;
            case Strong:
                return ItemCacheType.Strong;
            default:
                return ItemCacheType.Soft;
        }
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
                    singleItemHolderHelper.fieldHolderType = strongRefClassName;
                    break;
                case Soft:
                    singleItemHolderHelper.fieldHolderType = softRefClassName;
                    break;
                case Weak:
                    singleItemHolderHelper.fieldHolderType = weakRefClassName;
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
                    simpleMapItemHolderHelper.fieldHolderType = strongMapClassName;
                    break;
                case Soft:
                    simpleMapItemHolderHelper.fieldHolderType = softMapClassName;
                    break;
                case Weak:
                    simpleMapItemHolderHelper.fieldHolderType = weakMapClassName;
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
                multiKeyMapItemHolderHelper.fieldHolderType = strongMapClassName;
                break;
            case Soft:
                multiKeyMapItemHolderHelper.fieldHolderType = softMapClassName;
                break;
            case Weak:
                multiKeyMapItemHolderHelper.fieldHolderType = weakMapClassName;
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
        public ClassName fieldHolderType;

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
        public ClassName fieldHolderType;
        public FieldDetail keyParam;


        @Override
        public FieldSpec.Builder cachedField() {
            ParameterizedTypeName cacheType = ParameterizedTypeName.get(fieldHolderType, keyParam.type, fieldType);
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
        public ClassName fieldHolderType;

        public List<FieldDetail> keyArgs;


        @Override
        public FieldSpec.Builder cachedField() {
            ParameterizedTypeName cacheType = ParameterizedTypeName.get(fieldHolderType, multiKeyMapClassName, fieldType);
            return FieldSpec.builder(cacheType, fieldName, Modifier.PRIVATE, Modifier.FINAL)
                    .initializer("new $T()", cacheType);
        }

        @Override
        public CodeBlock codeGetCachedValue() {
            return CodeBlock.builder()
                    .add("$L.get(new $T($L) )", fieldName, multiKeyMapClassName,
                            String.join(",", ListUtils.format(keyArgs, (k) -> k.name)))
                    .build();
        }

        @Override
        public CodeBlock codeSetCachedValue(CodeBlock value) {
            return CodeBlock.of(
                    "$L.set( new $T( $L ), $L )", fieldName, multiKeyMapClassName,
                    String.join(",", ListUtils.format(keyArgs, (k) -> k.name)),
                    value
            );
        }

        @Override
        public CodeBlock codeSetCachedIfNullValue(CodeBlock value) {
            return CodeBlock.of(
                    "$L.setIfNull( new $T( $L ), $L )", fieldName, multiKeyMapClassName,
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
