package com.github.klee0kai.stone.checks;

import com.github.klee0kai.stone.exceptions.ComponentIncorrectSignatureException;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.MethodDetail;

public class ComponentChecks {

    public static void checkComponentClass(ClassDetail cl) {
        checkClassAnnotations(cl);
        checkClassNoHaveFields(cl);
        for (MethodDetail m : cl.getAllMethods(false, true))
            checkMethodSignature(m);
    }


    private static void checkClassAnnotations(ClassDetail cl) {
        if (cl.dependenciesAnn != null)
            throw new ComponentIncorrectSignatureException("Component's class " + cl.className + " should not have @Dependencies annotation");
        if (cl.moduleAnn != null)
            throw new ComponentIncorrectSignatureException("Component's class " + cl.className + " should not have @Module annotation");
        if (cl.wrapperCreatorsAnn != null)
            throw new ComponentIncorrectSignatureException("Component's class " + cl.className + " should not have @WrappersCreator annotation");
    }

    private static void checkMethodSignature(MethodDetail m) {
        if (m.provideAnnotation != null)
            throw new ComponentIncorrectSignatureException("Component's method " + m.methodName + " should not have @Provide annotation");
    }

    private static void checkClassNoHaveFields(ClassDetail cl) {
        if (!cl.fields.isEmpty())
            throw new ComponentIncorrectSignatureException("Component's class " + cl.className + " should not have fields");
    }

}
