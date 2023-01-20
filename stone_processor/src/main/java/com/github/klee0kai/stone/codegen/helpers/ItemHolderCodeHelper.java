package com.github.klee0kai.stone.codegen.helpers;

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
        Strong, Soft, Weak
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


    CodeBlock statementSwitchRef(String cacheFieldName, String timeSchedulerFieldName, String timeFieldName);


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
            return CodeBlock.builder()
                    .add("$L.set(", fieldName)
                    .add(value)
                    .add(")")
                    .build();
        }

        @Override
        public CodeBlock statementSwitchRef(String cacheFieldName, String timeSchedulerFieldName, String timeFieldName) {
            return CodeBlock.builder()
                    .addStatement("$L.switchCache($L,$L,$L)", fieldName, cacheFieldName, timeSchedulerFieldName, timeFieldName)
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
            return CodeBlock.builder()
                    .add("$L.set( $L , ", fieldName, keyParam.name)
                    .add(value)
                    .add(")")
                    .build();
        }

        @Override
        public CodeBlock statementSwitchRef(String cacheFieldName, String timeSchedulerFieldName, String timeFieldName) {
            return CodeBlock.builder()
                    .addStatement("$L.switchCache($L,$L,$L)", fieldName, cacheFieldName, timeSchedulerFieldName, timeFieldName)
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
            ;
            return CodeBlock.builder()
                    .add("$L.get(new $T($L) )", fieldName, multiKeyMapClassName,
                            String.join(",", ListUtils.format(keyArgs, (k) -> k.name)))
                    .build();
        }

        @Override
        public CodeBlock codeSetCachedValue(CodeBlock value) {
            return CodeBlock.builder()
                    .add("$L.set( new $T( $L ), ", fieldName, multiKeyMapClassName,
                            String.join(",", ListUtils.format(keyArgs, (k) -> k.name)))
                    .add(value)
                    .add(")")
                    .build();
        }

        @Override
        public CodeBlock statementSwitchRef(String cacheFieldName, String timeSchedulerFieldName, String timeFieldName) {
            return CodeBlock.builder()
                    .addStatement("$L.switchCache($L,$L,$L)", fieldName, cacheFieldName, timeSchedulerFieldName, timeFieldName)
                    .build();
        }

    }


}
