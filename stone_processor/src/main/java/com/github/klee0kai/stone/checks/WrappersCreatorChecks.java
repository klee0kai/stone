package com.github.klee0kai.stone.checks;

import com.github.klee0kai.stone.exceptions.WrappersCreatorIncorrectSignatureException;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.types.wrappers.IWrapperCreator;
import com.squareup.javapoet.ClassName;

import javax.lang.model.element.Modifier;
import java.util.Collections;
import java.util.Objects;

public class WrappersCreatorChecks {

    public static void checkWrapperClass(ClassDetail cl) {
        try {
            checkClassAnnotations(cl);
            checkIWrapperCreatorInterface(cl);
        } catch (Exception e) {
            throw new WrappersCreatorIncorrectSignatureException("WrappersCreator's class " + cl.className + " has incorrect signature", e);
        }
    }

    private static void checkClassAnnotations(ClassDetail cl) {
        if (cl.componentAnn != null)
            throw new WrappersCreatorIncorrectSignatureException("WrappersCreator's class " + cl.className + " should not have @Component annotation");
        if (cl.dependenciesAnn != null)
            throw new WrappersCreatorIncorrectSignatureException("WrappersCreator's class " + cl.className + " should not have @Dependencies annotation");
        if (cl.moduleAnn != null)
            throw new WrappersCreatorIncorrectSignatureException("WrappersCreator's class " + cl.className + " should not have @Module annotation");
    }

    private static void checkIWrapperCreatorInterface(ClassDetail cl) {
        ClassName wrClName = ClassName.get(IWrapperCreator.class);
        boolean isWrapperCreatorInterface = false;
        for (ClassDetail p : cl.getAllParents(false))
            if (Objects.equals(p.className, wrClName)) {
                isWrapperCreatorInterface = true;
                break;
            }
        if (!isWrapperCreatorInterface)
            throw new WrappersCreatorIncorrectSignatureException("WrappersCreator's class " + cl.className + " should implement IWrapperCreator interface");

        MethodDetail m = cl.findMethod(MethodDetail.constructorMethod(Collections.emptyList()), false);
        if (m == null)
            throw new WrappersCreatorIncorrectSignatureException("WrappersCreator's class " + cl.className + " should have constructor without parameters");
        if (!m.modifiers.contains(Modifier.PUBLIC))
            throw new WrappersCreatorIncorrectSignatureException("WrappersCreator's class " + cl.className + " should have public constructor");
    }

}
