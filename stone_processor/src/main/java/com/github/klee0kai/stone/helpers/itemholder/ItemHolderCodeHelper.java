package com.github.klee0kai.stone.helpers.itemholder;

import com.github.klee0kai.stone.closed.types.MultiKey;
import com.github.klee0kai.stone.closed.types.map.SoftMapItemHolder;
import com.github.klee0kai.stone.closed.types.map.StrongMapItemHolder;
import com.github.klee0kai.stone.closed.types.map.WeakMapItemHolder;
import com.github.klee0kai.stone.closed.types.single.SoftItemHolder;
import com.github.klee0kai.stone.closed.types.single.StrongItemHolder;
import com.github.klee0kai.stone.closed.types.single.WeakItemHolder;
import com.github.klee0kai.stone.model.FieldDetail;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;

import java.util.List;

import static com.github.klee0kai.stone.helpers.wrap.WrapHelper.isList;
import static com.github.klee0kai.stone.helpers.wrap.WrapHelper.nonWrappedType;

public interface ItemHolderCodeHelper {

    ClassName multiKeyClassName = ClassName.get(MultiKey.class);

    ClassName strongHolderClassName = ClassName.get(StrongItemHolder.class);
    ClassName softHolderClassName = ClassName.get(SoftItemHolder.class);
    ClassName weakHolderClassName = ClassName.get(WeakItemHolder.class);

    ClassName strongMapHolderClassName = ClassName.get(StrongMapItemHolder.class);
    ClassName softMapHolderClassName = ClassName.get(SoftMapItemHolder.class);
    ClassName weakMapHolderClassName = ClassName.get(WeakMapItemHolder.class);

    static ItemHolderCodeHelper of(
            String fieldName,
            TypeName returnType,
            List<FieldDetail> qualifiers,
            ItemCacheType cacheType
    ) {
        if (qualifiers == null || qualifiers.isEmpty()) {
            SingleItemHolderHelper singleItemHolderHelper = new SingleItemHolderHelper();
            singleItemHolderHelper.fieldName = fieldName;
            singleItemHolderHelper.nonWrappedType = nonWrappedType(returnType);
            singleItemHolderHelper.returnType = returnType;
            singleItemHolderHelper.isListCaching = isList(returnType);
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
            simpleMapItemHolderHelper.nonWrappedType = nonWrappedType(returnType);
            simpleMapItemHolderHelper.returnType = returnType;
            simpleMapItemHolderHelper.keyParam = qualifiers.get(0);
            simpleMapItemHolderHelper.isListCaching = isList(returnType);
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
        multiKeyMapItemHolderHelper.nonWrappedType = nonWrappedType(returnType);
        multiKeyMapItemHolderHelper.returnType = returnType;
        multiKeyMapItemHolderHelper.keyArgs = qualifiers;
        multiKeyMapItemHolderHelper.isListCaching = isList(returnType);
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

    CodeBlock nonNullCheck();

    CodeBlock codeGetCachedValue();

    CodeBlock codeSetCachedValue(CodeBlock value);

    CodeBlock codeSetCachedIfNullValue(CodeBlock value);

    CodeBlock statementSwitchRef(CodeBlock paramsCode);


}
