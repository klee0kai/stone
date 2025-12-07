package com.github.klee0kai.stone.helpers.itemholder;

import com.github.klee0kai.stone._hidden_.types.holders.MapItemHolder;
import com.github.klee0kai.stone._hidden_.types.holders.StoneRefType;
import com.github.klee0kai.stone.model.FieldDetail;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

public class SimpleMapItemHolderHelper implements ItemHolderCodeHelper {

    public String fieldName;
    public TypeName nonWrappedType;

    public TypeName returnType;
    public FieldDetail keyParam;

    public StoneRefType defRefType;

    public boolean isListCaching;


    @Override
    public FieldSpec.Builder cachedField() {
        ParameterizedTypeName cacheType = ParameterizedTypeName.get(ClassName.get(MapItemHolder.class), keyParam.type, nonWrappedType);
        return FieldSpec.builder(cacheType, fieldName, Modifier.PRIVATE, Modifier.FINAL)
                .initializer("new $T($T.$L)", cacheType, StoneRefType.class, defRefType.toString());
    }

    @Override
    public CodeBlock clearNullsStatement() {
        return CodeBlock.builder()
                .addStatement("$L.clearNulls()", fieldName)
                .build();
    }

    @Override
    public CodeBlock codeGetCachedValue() {
        String getMethod = isListCaching ? "getList" : "get";
        return CodeBlock.of("$L.$L($L)", fieldName, getMethod, keyParam.name);
    }

    @Override
    public CodeBlock codeSetCachedValue(CodeBlock value, boolean isOnlyIfNeed) {
        String setMethod = isListCaching ? "setList" : "set";
        return CodeBlock.builder()
                .add(CodeBlock.of("$L.$L( $L, () -> ", fieldName, setMethod, keyParam.name))
                .add(value)
                .add(CodeBlock.of(", $L )", isOnlyIfNeed))
                .build();
    }

    @Override
    public CodeBlock statementSwitchRef(CodeBlock paramsCode) {
        return CodeBlock.builder()
                .addStatement("$L.switchCache($L)", fieldName, paramsCode)
                .build();
    }


}
