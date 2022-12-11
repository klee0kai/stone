package com.github.klee0kai.stone.codegen.helpers;

import com.github.klee0kai.stone.annotations.component.SwitchCache;
import com.squareup.javapoet.CodeBlock;

public class ModuleFieldHelper {

    public String moduleFieldName;

    public ModuleFieldHelper(String moduleFieldName) {
        this.moduleFieldName = moduleFieldName;
    }


    public CodeBlock statementAllWeak(String scopesField) {
        return CodeBlock.builder()
                .addStatement("this.$L.switchRef($L, $T.Weak , null, -1)", moduleFieldName, scopesField, SwitchCache.CacheType.class)
                .build();
    }

    public CodeBlock statementAllDef(String scopesField) {
        return CodeBlock.builder()
                .addStatement("this.$L.switchRef($L, $T.Default, null, -1 )", moduleFieldName, scopesField, SwitchCache.CacheType.class)
                .build();
    }

    public CodeBlock statementSwitchRefs(String scopesField, String cacheTypeField, String scheduleField, String timeField) {
        return CodeBlock.builder()
                .addStatement("this.$L.switchRef($L, $L, $L , $L )", moduleFieldName, scopesField, cacheTypeField, scheduleField, timeField)
                .build();
    }

}
