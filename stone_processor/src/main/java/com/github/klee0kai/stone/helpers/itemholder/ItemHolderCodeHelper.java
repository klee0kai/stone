package com.github.klee0kai.stone.helpers.itemholder;

import com.github.klee0kai.stone._hidden_.types.MultiKey;
import com.github.klee0kai.stone.helpers.wrap.WrapHelper;
import com.github.klee0kai.stone.model.FieldDetail;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;

import java.util.List;

public interface ItemHolderCodeHelper {

    ClassName multiKeyClassName = ClassName.get(MultiKey.class);

    static ItemHolderCodeHelper of(
            String fieldName,
            TypeName returnType,
            List<FieldDetail> idFields,
            ItemCacheType cacheType,
            WrapHelper wrapHelper
    ) {
        if (idFields == null || idFields.isEmpty()) {
            SingleItemHolderHelper singleItemHolderHelper = new SingleItemHolderHelper();
            singleItemHolderHelper.fieldName = fieldName;
            singleItemHolderHelper.nonWrappedType = wrapHelper.nonWrappedType(returnType);
            singleItemHolderHelper.returnType = returnType;
            singleItemHolderHelper.isListCaching = wrapHelper.isList(returnType);
            singleItemHolderHelper.defRefType = wrapHelper.isList(returnType) ? cacheType.toRefTypeList() : cacheType.toRefTypeSingle();
            return singleItemHolderHelper;
        }

        if (idFields.size() == 1) {
            SimpleMapItemHolderHelper simpleMapItemHolderHelper = new SimpleMapItemHolderHelper();
            simpleMapItemHolderHelper.fieldName = fieldName;
            simpleMapItemHolderHelper.nonWrappedType = wrapHelper.nonWrappedType(returnType);
            simpleMapItemHolderHelper.returnType = returnType;
            simpleMapItemHolderHelper.keyParam = idFields.get(0);
            simpleMapItemHolderHelper.isListCaching = wrapHelper.isList(returnType);
            simpleMapItemHolderHelper.defRefType = wrapHelper.isList(returnType) ? cacheType.toRefTypeList() : cacheType.toRefTypeSingle();
            return simpleMapItemHolderHelper;
        }

        MultiKeyMapItemHolderHelper multiKeyMapItemHolderHelper = new MultiKeyMapItemHolderHelper();
        multiKeyMapItemHolderHelper.fieldName = fieldName;
        multiKeyMapItemHolderHelper.nonWrappedType = wrapHelper.nonWrappedType(returnType);
        multiKeyMapItemHolderHelper.returnType = returnType;
        multiKeyMapItemHolderHelper.keyArgs = idFields;
        multiKeyMapItemHolderHelper.isListCaching = wrapHelper.isList(returnType);
        multiKeyMapItemHolderHelper.defRefType = wrapHelper.isList(returnType) ? cacheType.toRefTypeList() : cacheType.toRefTypeSingle();

        return multiKeyMapItemHolderHelper;
    }

    FieldSpec.Builder cachedField();

    CodeBlock clearNullsStatement();

    CodeBlock codeGetCachedValue();

    CodeBlock codeSetCachedValue(CodeBlock value, boolean onlyIfNull);

    CodeBlock statementSwitchRef(CodeBlock paramsCode);


}
