package com.github.klee0kai.stone.checks;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.component.ProtectInjected;
import com.github.klee0kai.stone.annotations.component.SwitchCache;
import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.wrappers.WrappersCreator;
import com.github.klee0kai.stone.exceptions.IncorrectSignatureException;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.MethodDetail;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import static com.github.klee0kai.stone.exceptions.StoneExceptionStrings.*;

public class DependencyChecks {

    public static void checkDependencyClass(ClassDetail cl) {
        try {
            checkClassAnnotations(cl);
            checkClassNoHaveFields(cl);
            for (MethodDetail m : cl.getAllMethods(false, true))
                checkMethodSignature(m);
        } catch (Exception e) {
            throw new IncorrectSignatureException(String.format(dependencyClass + hasIncorrectSignature, cl.className), e);
        }
    }

    private static void checkClassAnnotations(ClassDetail cl) {
        if (cl.componentAnn != null)
            throw new IncorrectSignatureException(String.format(dependencyClass + shouldNoHaveAnnotation, cl.className, Component.class.getSimpleName()));
        if (cl.moduleAnn != null)
            throw new IncorrectSignatureException(String.format(dependencyClass + shouldNoHaveAnnotation, cl.className, Module.class.getSimpleName()));
        if (cl.wrapperCreatorsAnn != null)
            throw new IncorrectSignatureException(String.format(dependencyClass + shouldNoHaveAnnotation, cl.className, WrappersCreator.class.getSimpleName()));
    }


    private static void checkMethodSignature(MethodDetail m) {
        if (m.injectAnnotation != null)
            throw new IncorrectSignatureException(String.format(method + shouldNoHaveAnnotation, m.methodName, Inject.class.getSimpleName()));
        if (m.protectInjectedAnnotation != null)
            throw new IncorrectSignatureException(String.format(method + shouldNoHaveAnnotation, m.methodName, ProtectInjected.class.getSimpleName()));

        if (m.switchCacheAnnotation != null)
            throw new IncorrectSignatureException(String.format(method + shouldNoHaveAnnotation, m.methodName, SwitchCache.class.getSimpleName()));

        //non support
        if (m.namedAnnotation != null)
            throw new IncorrectSignatureException(String.format(method + shouldNoHaveAnnotation, m.methodName, Named.class.getSimpleName()));

        if (m.singletonAnnotation != null)
            throw new IncorrectSignatureException(String.format(method + shouldNoHaveAnnotation, m.methodName, Singleton.class.getSimpleName()));
    }

    private static void checkClassNoHaveFields(ClassDetail cl) {
        if (!cl.fields.isEmpty())
            throw new IncorrectSignatureException(String.format(dependencyClass + shouldNoHaveFields, cl.className));
    }

}
