package com.github.klee0kai.stone.helpers.wrap;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

public interface FormatInList {

    /**
     * @param originalListCode original list code
     * @param itemTransformFun format each type
     * @return
     */
    CodeBlock formatCode(TypeName originalListType, CodeBlock originalListCode, FormatSimple itemTransformFun);

}
