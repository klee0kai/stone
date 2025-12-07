package com.github.klee0kai.stone.helpers.wrap;

import com.squareup.javapoet.CodeBlock;

public interface FormatSimple {

    /**
     * @param code code witch return original type
     * @return code witch return wanna type
     */
    CodeBlock formatCode(CodeBlock code);

}
