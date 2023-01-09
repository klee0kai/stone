package com.github.klee0kai.stone.codegen.helpers;

import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.FieldDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.squareup.javapoet.CodeBlock;

import java.util.Locale;

public class SetFieldHelper {

    private final FieldDetail fieldDetail;
    private MethodDetail kotlinGetMethod = null, kotlinSetMethod = null;

    /**
     * @param fieldDetail
     */
    public SetFieldHelper(FieldDetail fieldDetail) {
        this.fieldDetail = fieldDetail;
    }

    /**
     * set value code
     *
     * @param fieldOwner class, where field is declared
     * @param valueCode  set value code
     * @return
     */
    public CodeBlock codeSetField(String fieldOwner, CodeBlock valueCode) {
        if (isKotlinField()) {
            return CodeBlock.of(
                    "$L.$L( $L )",
                    fieldOwner, kotlinSetMethod.methodName, valueCode
            );
        } else {
            return CodeBlock.of(
                    "$L.$L = $L",
                    fieldOwner, fieldDetail.name, valueCode
            );
        }
    }


    /**
     * get original value code
     *
     * @param fieldOwner class, where field is declared
     * @return
     */
    public CodeBlock codeGetField(String fieldOwner) {
        if (isKotlinField()) {
            return CodeBlock.of("$L.$L()", fieldOwner, kotlinGetMethod.methodName);
        } else {
            return CodeBlock.of("$L.$L", fieldOwner, fieldDetail.name);
        }
    }

    /**
     * get* set* methods for check
     *
     * @param fieldOwner class, where field is declared
     * @return
     */
    public boolean checkIsKotlinField(ClassDetail fieldOwner) {
        String capitalizedName = fieldDetail.name.substring(0, 1).toUpperCase(Locale.ROOT)
                + fieldDetail.name.substring(1);
        MethodDetail getMethod = fieldOwner.findMethod(
                MethodDetail.simpleGetMethod("get" + capitalizedName, fieldDetail.type),
                true
        );
        MethodDetail setMethod = fieldOwner.findMethod(
                MethodDetail.simpleSetMethod("set" + capitalizedName, fieldDetail.type),
                true
        );

        kotlinGetMethod = fieldOwner.findMethod(getMethod, true);
        kotlinSetMethod = fieldOwner.findMethod(setMethod, true);
        return isKotlinField();

    }

    private boolean isKotlinField() {
        return kotlinGetMethod != null && kotlinSetMethod != null;
    }


}
