package com.github.klee0kai.stone.checks;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.dependencies.Dependencies;
import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.exceptions.IncorrectSignatureException;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.types.wrappers.IWrapperCreator;
import com.squareup.javapoet.ClassName;

import javax.lang.model.element.Modifier;
import java.util.Collections;
import java.util.Objects;

import static com.github.klee0kai.stone.exceptions.StoneExceptionStrings.*;

public class WrappersCreatorChecks {

    public static void checkWrapperClass(ClassDetail cl) {
        try {
            checkClassAnnotations(cl);
            checkIWrapperCreatorInterface(cl);
        } catch (Exception e) {
            throw new IncorrectSignatureException("WrappersCreator's class " + cl.className + " has incorrect signature", e);
        }
    }

    private static void checkClassAnnotations(ClassDetail cl) {
        if (cl.componentAnn != null)
            throw new IncorrectSignatureException(String.format(wrappersProviderClass + shouldNoHaveAnnotation, cl.className, Component.class.getSimpleName()));
        if (cl.dependenciesAnn != null)
            throw new IncorrectSignatureException(String.format(wrappersProviderClass + shouldNoHaveAnnotation, cl.className, Dependencies.class.getSimpleName()));
        if (cl.moduleAnn != null)
            throw new IncorrectSignatureException(String.format(wrappersProviderClass + shouldNoHaveAnnotation, cl.className, Module.class.getSimpleName()));
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
            throw new IncorrectSignatureException(String.format(wrappersProviderClass + shouldImplementInterface, cl.className, IWrapperCreator.class.getCanonicalName()));

        MethodDetail m = cl.findMethod(MethodDetail.constructorMethod(Collections.emptyList()), false);
        if (m == null || !m.modifiers.contains(Modifier.PUBLIC))
            throw new IncorrectSignatureException(String.format(wrappersProviderClass + shouldHaveConstructorWithoutArgs, cl.className));
    }

}
