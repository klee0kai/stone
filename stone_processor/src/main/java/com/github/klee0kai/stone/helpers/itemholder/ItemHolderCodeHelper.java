package com.github.klee0kai.stone.helpers.itemholder;

import com.github.klee0kai.stone._hidden_.types.MultiKey;
import com.github.klee0kai.stone.helpers.codebuilder.SmartCode;
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

    static ItemHolderCodeHelper of(
            String fieldName,
            TypeName returnType,
            List<FieldDetail> idFields,
            ItemCacheType cacheType
    ) {
        if (idFields == null || idFields.isEmpty()) {
            SingleItemHolderHelper singleItemHolderHelper = new SingleItemHolderHelper();
            singleItemHolderHelper.fieldName = fieldName;
            singleItemHolderHelper.nonWrappedType = nonWrappedType(returnType);
            singleItemHolderHelper.returnType = returnType;
            singleItemHolderHelper.isListCaching = isList(returnType);
            singleItemHolderHelper.defRefType = isList(returnType) ? cacheType.toRefTypeList() : cacheType.toRefTypeSingle();
            return singleItemHolderHelper;
        }

        if (idFields.size() == 1) {
            SimpleMapItemHolderHelper simpleMapItemHolderHelper = new SimpleMapItemHolderHelper();
            simpleMapItemHolderHelper.fieldName = fieldName;
            simpleMapItemHolderHelper.nonWrappedType = nonWrappedType(returnType);
            simpleMapItemHolderHelper.returnType = returnType;
            simpleMapItemHolderHelper.keyParam = idFields.get(0);
            simpleMapItemHolderHelper.isListCaching = isList(returnType);
            simpleMapItemHolderHelper.defRefType = isList(returnType) ? cacheType.toRefTypeList() : cacheType.toRefTypeSingle();
            return simpleMapItemHolderHelper;
        }

        MultiKeyMapItemHolderHelper multiKeyMapItemHolderHelper = new MultiKeyMapItemHolderHelper();
        multiKeyMapItemHolderHelper.fieldName = fieldName;
        multiKeyMapItemHolderHelper.nonWrappedType = nonWrappedType(returnType);
        multiKeyMapItemHolderHelper.returnType = returnType;
        multiKeyMapItemHolderHelper.keyArgs = idFields;
        multiKeyMapItemHolderHelper.isListCaching = isList(returnType);
        multiKeyMapItemHolderHelper.defRefType = isList(returnType) ? cacheType.toRefTypeList() : cacheType.toRefTypeSingle();

        return multiKeyMapItemHolderHelper;
    }

    FieldSpec.Builder cachedField();

    CodeBlock clearNullsStatement();

    SmartCode codeGetCachedValue();

    CodeBlock codeSetCachedValue(CodeBlock value, boolean onlyIfNull);

    CodeBlock statementSwitchRef(CodeBlock paramsCode);


}
