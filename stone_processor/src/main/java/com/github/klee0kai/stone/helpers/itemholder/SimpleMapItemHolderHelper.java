package com.github.klee0kai.stone.helpers.itemholder;

import com.github.klee0kai.stone.closed.types.holders.MapItemHolder;
import com.github.klee0kai.stone.closed.types.holders.ItemRefType;
import com.github.klee0kai.stone.helpers.codebuilder.SmartCode;
import com.github.klee0kai.stone.model.FieldDetail;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

import static com.github.klee0kai.stone.helpers.wrap.WrapHelper.listWrapTypeIfNeed;

public class SimpleMapItemHolderHelper implements ItemHolderCodeHelper {

    public String fieldName;
    public TypeName nonWrappedType;

    public TypeName returnType;
    public FieldDetail keyParam;

    public ItemRefType defRefType;

    public boolean isListCaching;


    @Override
    public FieldSpec.Builder cachedField() {
        ParameterizedTypeName cacheType = ParameterizedTypeName.get(ClassName.get(MapItemHolder.class), keyParam.type, nonWrappedType);
        return FieldSpec.builder(cacheType, fieldName, Modifier.PRIVATE, Modifier.FINAL)
                .initializer("new $T($T.$L)", cacheType, ItemRefType.class, defRefType.toString());
    }

    @Override
    public CodeBlock clearNullsStatement() {
        return CodeBlock.builder()
                .addStatement("$L.clearNulls()", fieldName)
                .build();
    }

    @Override
    public SmartCode codeGetCachedValue() {
        String getMethod = isListCaching ? "getList" : "get";
        return SmartCode.of(CodeBlock.of("$L.$L($L)", fieldName, getMethod, keyParam.name))
                .providingType(listWrapTypeIfNeed(returnType));
    }

    @Override
    public CodeBlock codeSetCachedValue(CodeBlock value, boolean isOnlyIfNeed) {
        String setMethod = isListCaching ? "setList" : "set";
        return SmartCode.builder()
                .add(CodeBlock.of("$L.$L( $L, () -> ", fieldName, setMethod, keyParam.name))
                .add(value)
                .add(CodeBlock.of(", $L )", isOnlyIfNeed))
                .build(null);
    }

    @Override
    public CodeBlock statementSwitchRef(CodeBlock paramsCode) {
        return CodeBlock.builder()
                .addStatement("$L.switchCache($L)", fieldName, paramsCode)
                .build();
    }


}
