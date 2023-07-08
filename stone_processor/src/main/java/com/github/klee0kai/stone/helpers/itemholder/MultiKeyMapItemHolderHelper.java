package com.github.klee0kai.stone.helpers.itemholder;

import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.stone.helpers.codebuilder.SmartCode;
import com.github.klee0kai.stone.model.FieldDetail;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.List;

import static com.github.klee0kai.stone.helpers.wrap.WrapHelper.transform;

public class MultiKeyMapItemHolderHelper implements ItemHolderCodeHelper {

    public String fieldName;
    public TypeName nonWrappedType;

    public TypeName returnType;
    public ClassName holderType;

    public List<FieldDetail> keyArgs;

    public boolean isListCaching;


    @Override
    public FieldSpec.Builder cachedField() {
        ParameterizedTypeName cacheType = ParameterizedTypeName.get(holderType, multiKeyClassName, nonWrappedType);
        return FieldSpec.builder(cacheType, fieldName, Modifier.PRIVATE, Modifier.FINAL)
                .initializer("new $T()", cacheType);
    }

    @Override
    public CodeBlock nonNullCheck() {
        String getMethod = isListCaching ? "getList" : "get";
        return CodeBlock.of("$L.$L(new $T($L) ) != null", fieldName, getMethod, multiKeyClassName,
                String.join(",", ListUtils.format(keyArgs, (k) -> k.name)));
    }

    @Override
    public CodeBlock codeGetCachedValue() {
        String getMethod = isListCaching ? "getList" : "get";
        return transform(
                SmartCode.of(CodeBlock.of("$L.$L(new $T($L) )", fieldName, getMethod, multiKeyClassName,
                                String.join(",", ListUtils.format(keyArgs, (k) -> k.name))))
                        .providingType(providingType()),
                returnType).build(null);
    }

    @Override
    public CodeBlock codeSetCachedValue(CodeBlock value) {
        String setMethod = isListCaching ? "setList" : "set";
        return SmartCode.builder()
                .add(CodeBlock.of("$L.$L( new $T( $L ), ", fieldName, setMethod, multiKeyClassName,
                        String.join(",", ListUtils.format(keyArgs, (k) -> k.name))))
                .add(transform(
                        SmartCode.of(value).providingType(returnType),
                        providingType()
                )).add(")")
                .build(null);
    }

    @Override
    public CodeBlock codeSetCachedIfNullValue(CodeBlock value) {
        String setMethod = isListCaching ? "setListIfNull" : "setIfNull";
        return SmartCode.builder()
                .add(CodeBlock.of("$L.$L( new $T( $L ), ", fieldName, setMethod, multiKeyClassName,
                        String.join(",", ListUtils.format(keyArgs, (k) -> k.name))))
                .add(transform(
                        SmartCode.of(value).providingType(returnType),
                        providingType()
                )).add(")")
                .build(null);
    }

    @Override
    public CodeBlock statementSwitchRef(CodeBlock paramsCode) {
        return CodeBlock.builder()
                .addStatement("$L.switchCache($L)", fieldName, paramsCode)
                .build();
    }

    private TypeName providingType() {
        return isListCaching ? ParameterizedTypeName.get(ClassName.get(List.class), nonWrappedType) : nonWrappedType;
    }


}
