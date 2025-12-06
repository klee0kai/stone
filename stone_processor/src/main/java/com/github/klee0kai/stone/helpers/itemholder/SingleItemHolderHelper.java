package com.github.klee0kai.stone.helpers.itemholder;

import com.github.klee0kai.stone._hidden_.types.holders.SingleItemHolder;
import com.github.klee0kai.stone._hidden_.types.holders.StoneRefType;
import com.github.klee0kai.stone.helpers.codebuilder.SmartCode;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

public class SingleItemHolderHelper implements ItemHolderCodeHelper {

    public String fieldName;
    public TypeName nonWrappedType;
    public TypeName returnType;

    public StoneRefType defRefType;
    public boolean isListCaching;


    @Override
    public FieldSpec.Builder cachedField() {
        ParameterizedTypeName cacheType = ParameterizedTypeName.get(ClassName.get(SingleItemHolder.class), nonWrappedType);
        return FieldSpec.builder(cacheType, fieldName, Modifier.PRIVATE, Modifier.FINAL)
                .initializer("new $T($T.$L)", cacheType, StoneRefType.class, defRefType.toString());
    }

    @Override
    public CodeBlock clearNullsStatement() {
        return CodeBlock.of("");
    }

    @Override
    public CodeBlock codeGetCachedValue() {
        String getMethod = isListCaching ? "getList" : "get";
        return CodeBlock.of("$L.$L()", fieldName, getMethod);
    }

    @Override
    public CodeBlock codeSetCachedValue(CodeBlock value, boolean onlyIfNull) {
        String setMethod = isListCaching ? "setList" : "set";
        return SmartCode.builder()
                .add(CodeBlock.of("$L.$L( ()-> ", fieldName, setMethod))
                .add(value)
                .add(CodeBlock.of(", $L)", onlyIfNull))
                .build(null);
    }

    @Override
    public CodeBlock statementSwitchRef(CodeBlock paramsCode) {
        return CodeBlock.builder()
                .addStatement("$L.switchCache($L)", fieldName, paramsCode)
                .build();
    }


}
