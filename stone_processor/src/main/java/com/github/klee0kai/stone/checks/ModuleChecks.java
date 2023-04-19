package com.github.klee0kai.stone.checks;

import com.github.klee0kai.stone.exceptions.ComponentIncorrectSignatureException;
import com.github.klee0kai.stone.exceptions.ModuleIncorrectSignatureException;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.MethodDetail;

public class ModuleChecks {

    public static void checkModuleClass(ClassDetail cl) {
        checkClassAnnotations(cl);
        checkClassNoHaveFields(cl);
        for (MethodDetail m : cl.getAllMethods(false, true))
            checkMethodSignature(m);
    }

    private static void checkClassAnnotations(ClassDetail cl) {
        if (cl.componentAnn != null)
            throw new ModuleIncorrectSignatureException("Module's class " + cl.className + " should not have @Component annotation");
        if (cl.dependenciesAnn != null)
            throw new ComponentIncorrectSignatureException("Module's class " + cl.className + " should not have @Dependencies annotation");
        if (cl.wrapperCreatorsAnn != null)
            throw new ComponentIncorrectSignatureException("Module's class " + cl.className + " should not have @WrappersCreator annotation");
    }


    private static void checkMethodSignature(MethodDetail m) {
        if (m.injectAnnotation != null)
            throw new ModuleIncorrectSignatureException("Module's method " + m.methodName + " should not have @Inject annotation");
        if (m.protectInjectedAnnotation != null)
            throw new ModuleIncorrectSignatureException("Module's method " + m.methodName + " should not have @ProtectInjected annotation");
        if (m.switchCacheAnnotation != null)
            throw new ModuleIncorrectSignatureException("Module's method " + m.methodName + " should not have @SwitchCache annotation");

        //non support
        if (m.namedAnnotation != null)
            throw new ModuleIncorrectSignatureException("Module's method " + m.methodName + " not support @Named annotation");
        if (m.singletonAnnotation != null)
            throw new ModuleIncorrectSignatureException("Module's method " + m.methodName + " not support @Singleton annotation");
    }

    private static void checkClassNoHaveFields(ClassDetail cl) {
        if (!cl.fields.isEmpty())
            throw new ModuleIncorrectSignatureException("Module's class " + cl.className + " should not have fields");
    }

}
