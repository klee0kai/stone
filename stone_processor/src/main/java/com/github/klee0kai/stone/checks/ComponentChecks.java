package com.github.klee0kai.stone.checks;

import com.github.klee0kai.stone.AnnotationProcessor;
import com.github.klee0kai.stone.exceptions.ComponentIncorrectSignatureException;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.squareup.javapoet.ClassName;

import java.util.Objects;

public class ComponentChecks {

    public static void checkComponentClass(ClassDetail cl) {
        try {
            checkClassAnnotations(cl);
            checkClassNoHaveFields(cl);
            for (MethodDetail m : cl.getAllMethods(false, true))
                checkMethodSignature(m);
        } catch (Exception e) {
            throw new ComponentIncorrectSignatureException("Component's class " + cl.className + " has incorrect signature", e);
        }
    }


    private static void checkClassAnnotations(ClassDetail cl) {
        if (cl.wrapperCreatorsAnn != null)
            throw new ComponentIncorrectSignatureException("Component's class " + cl.className + " should not have @WrappersCreator annotation");

        for (ClassName q : cl.componentAnn.qualifiers) {
            if (q.isPrimitive())
                throw new ComponentIncorrectSignatureException("Component's class " + cl.className + " should not have primitive qualifier");
            if (Objects.equals(q, ClassName.OBJECT))
                throw new ComponentIncorrectSignatureException("Component's class " + cl.className + " should not have Object qualifier");
        }

        for (ClassName wr : cl.componentAnn.wrapperProviders) {
            ClassDetail wrCl = AnnotationProcessor.allClassesHelper.findForType(wr);
            WrappersCreatorChecks.checkWrapperClass(wrCl);
        }

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
