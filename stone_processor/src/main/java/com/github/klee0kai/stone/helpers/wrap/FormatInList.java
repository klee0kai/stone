package com.github.klee0kai.stone.helpers.wrap;

import com.github.klee0kai.stone.helpers.codebuilder.SmartCode;
import com.squareup.javapoet.TypeName;

public interface FormatInList {

    /**
     * @param originalListCode original list code
     * @param itemTransformFun format each type
     * @return
     */
    SmartCode formatCode(TypeName originalListType, SmartCode originalListCode, FormatSimple itemTransformFun);

}
