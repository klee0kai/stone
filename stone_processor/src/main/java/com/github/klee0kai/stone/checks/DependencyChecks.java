package com.github.klee0kai.stone.checks;

import com.github.klee0kai.stone.exceptions.DependencyIncorrectSignatureException;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.MethodDetail;

public class DependencyChecks {

    public static void checkDependencyClass(ClassDetail cl) {
        try {
            checkClassAnnotations(cl);
            checkClassNoHaveFields(cl);
            for (MethodDetail m : cl.getAllMethods(false, true))
                checkMethodSignature(m);
        } catch (Exception e) {
            throw new DependencyIncorrectSignatureException("Dependency's class " + cl.className + " has incorrect signature", e);
        }
    }

    private static void checkClassAnnotations(ClassDetail cl) {
        if (cl.componentAnn != null)
            throw new DependencyIncorrectSignatureException("Dependency's class " + cl.className + " should not have @Component annotation");
        if (cl.moduleAnn != null)
            throw new DependencyIncorrectSignatureException("Dependency's class " + cl.className + " should not have @Module annotation");
        if (cl.wrapperCreatorsAnn != null)
            throw new DependencyIncorrectSignatureException("Dependency's class " + cl.className + " should not have @WrappersCreator annotation");
    }


    private static void checkMethodSignature(MethodDetail m) {
        if (m.injectAnnotation != null)
            throw new DependencyIncorrectSignatureException("Dependency's method " + m.methodName + " should not have @Inject annotation");
        if (m.protectInjectedAnnotation != null)
            throw new DependencyIncorrectSignatureException("Dependency's method " + m.methodName + " should not have @ProtectInjected annotation");
        if (m.switchCacheAnnotation != null)
            throw new DependencyIncorrectSignatureException("Dependency's method " + m.methodName + " should not have @SwitchCache annotation");

        //non support
        if (m.namedAnnotation != null)
            throw new DependencyIncorrectSignatureException("Dependency's method " + m.methodName + " not support @Named annotation");
        if (m.singletonAnnotation != null)
            throw new DependencyIncorrectSignatureException("Dependency's method " + m.methodName + " not support @Singleton annotation");
    }

    private static void checkClassNoHaveFields(ClassDetail cl) {
        if (!cl.fields.isEmpty())
            throw new DependencyIncorrectSignatureException("Dependency's class " + cl.className + " should not have fields");
    }

}
