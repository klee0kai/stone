package com.github.klee0kai.stone.helpers;

import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.FieldDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.squareup.javapoet.CodeBlock;

import java.util.Locale;

public class SetFieldHelper {

    private final FieldDetail fieldDetail;
    private MethodDetail kotlinGetMethod = null, kotlinSetMethod = null;

    public static String capitalized(String str) {
        return str.substring(0, 1).toUpperCase(Locale.ROOT) + str.substring(1);
    }

    /**
     * @param fieldDetail
     * @param fieldOwner  class where field is declare
     */
    public SetFieldHelper(FieldDetail fieldDetail, ClassDetail fieldOwner) {
        this.fieldDetail = fieldDetail;

        // check kotlin field
        String capitalizedName = capitalized(fieldDetail.name);
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
    }

    /**
     * set value code
     *
     * @param valueCode set value code
     * @return
     */
    public CodeBlock codeSetField(CodeBlock valueCode) {
        if (isKotlinField()) {
            return CodeBlock.of(
                    "$L( $L )",
                    kotlinSetMethod.methodName, valueCode
            );
        } else {
            return CodeBlock.of(
                    "$L = $L",
                    fieldDetail.name, valueCode
            );
        }
    }


    /**
     * get original value code
     *
     * @return
     */
    public CodeBlock codeGetField() {
        if (isKotlinField()) {
            return CodeBlock.of("$L()", kotlinGetMethod.methodName);
        } else {
            return CodeBlock.of("$L", fieldDetail.name);
        }
    }

    private boolean isKotlinField() {
        return kotlinGetMethod != null && kotlinSetMethod != null;
    }


}
