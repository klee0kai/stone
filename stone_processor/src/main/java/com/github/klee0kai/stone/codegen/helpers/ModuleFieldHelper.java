package com.github.klee0kai.stone.codegen.helpers;

import com.squareup.javapoet.CodeBlock;

public class ModuleFieldHelper {

    public String moduleFieldName;

    public ModuleFieldHelper(String moduleFieldName) {
        this.moduleFieldName = moduleFieldName;
    }


    public CodeBlock statementSwitchRefs(String scopesField, String switchCacheParams) {
        return CodeBlock.builder()
                .addStatement("this.$L.switchRef($L, $L)", moduleFieldName, scopesField, switchCacheParams)
                .build();
    }

}
