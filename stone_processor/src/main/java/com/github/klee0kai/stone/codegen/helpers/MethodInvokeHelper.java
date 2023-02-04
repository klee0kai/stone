package com.github.klee0kai.stone.codegen.helpers;

import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.stone.model.FieldDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.squareup.javapoet.CodeBlock;

public class MethodInvokeHelper {


    public static  CodeBlock sameMethodInvokeCode(MethodDetail m) {
        return CodeBlock.of(
                "$L($L)",
                m.methodName,
                String.join(",", ListUtils.format(m.args, (ListUtils.IFormat<FieldDetail, CharSequence>) it -> it.name)
                ));
    }

}
