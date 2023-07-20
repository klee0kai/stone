package com.github.klee0kai.stone.helpers.itemholder;

import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.stone.closed.types.holders.StMapItemHolder;
import com.github.klee0kai.stone.closed.types.holders.StRefType;
import com.github.klee0kai.stone.helpers.codebuilder.SmartCode;
import com.github.klee0kai.stone.model.FieldDetail;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.List;

import static com.github.klee0kai.stone.helpers.wrap.WrapHelper.listWrapTypeIfNeed;

public class MultiKeyMapItemHolderHelper implements ItemHolderCodeHelper {

    public String fieldName;
    public TypeName nonWrappedType;
    public TypeName returnType;
    public StRefType defRefType;
    public List<FieldDetail> keyArgs;
    public boolean isListCaching;


    @Override
    public FieldSpec.Builder cachedField() {
        ParameterizedTypeName cacheType = ParameterizedTypeName.get(ClassName.get(StMapItemHolder.class), multiKeyClassName, nonWrappedType);
        return FieldSpec.builder(cacheType, fieldName, Modifier.PRIVATE, Modifier.FINAL)
                .initializer("new $T($T.$L)", cacheType, StRefType.class, defRefType.toString());
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
        return SmartCode.of(CodeBlock.of("$L.$L(new $T($L) )", fieldName, getMethod, multiKeyClassName,
                        String.join(",", ListUtils.format(keyArgs, (k) -> k.name))))
                .providingType(listWrapTypeIfNeed(returnType));
    }

    @Override
    public CodeBlock codeSetCachedValue(CodeBlock value, boolean isOnlyIfNeed) {
        String setMethod = isListCaching ? "setList" : "set";
        return SmartCode.builder()
                .add(CodeBlock.of("$L.$L( new $T( $L ), () ->  ", fieldName, setMethod, multiKeyClassName,
                        String.join(",", ListUtils.format(keyArgs, (k) -> k.name))))
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
