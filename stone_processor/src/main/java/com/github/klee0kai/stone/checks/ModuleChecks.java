package com.github.klee0kai.stone.checks;

import com.github.klee0kai.stone.annotations.component.ProtectInjected;
import com.github.klee0kai.stone.annotations.component.SwitchCache;
import com.github.klee0kai.stone.exceptions.IncorrectSignatureException;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.FieldDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.model.annotations.ComponentAnn;
import com.github.klee0kai.stone.model.annotations.DependenciesAnn;
import com.github.klee0kai.stone.model.annotations.IAnnotation;
import com.github.klee0kai.stone.model.annotations.WrapperCreatorsAnn;
import com.squareup.javapoet.TypeName;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Objects;

import static com.github.klee0kai.stone.exceptions.StoneExceptionStrings.*;

public class ModuleChecks {

    public static void checkModuleClass(ClassDetail cl) {
        try {
            checkClassAnnotations(cl);
            checkClassNoHaveFields(cl);
            for (MethodDetail m : cl.getAllMethods(false, true))
                checkMethodSignature(m);
        } catch (Exception e) {
            throw new IncorrectSignatureException(String.format(moduleClass + hasIncorrectSignature, cl.className), e);
        }
    }

    private static void checkClassAnnotations(ClassDetail cl) {
        IAnnotation prohibitedAnn = cl.anyAnnotation(ComponentAnn.class, DependenciesAnn.class, WrapperCreatorsAnn.class);
        if (prohibitedAnn != null)
            throw new IncorrectSignatureException(String.format(
                    dependencyClass + shouldNoHaveAnnotation,
                    cl.className, prohibitedAnn.originalAnn().getSimpleName()
            ));
    }


    private static void checkMethodSignature(MethodDetail m) {
        if (m.injectAnnotation != null)
            throw new IncorrectSignatureException(String.format(method + shouldNoHaveAnnotation, m.methodName, Inject.class.getSimpleName()));
        if (m.protectInjectedAnn != null)
            throw new IncorrectSignatureException(String.format(method + shouldNoHaveAnnotation, m.methodName, ProtectInjected.class.getSimpleName()));
        if (m.switchCacheAnn != null)
            throw new IncorrectSignatureException(String.format(method + shouldNoHaveAnnotation, m.methodName, SwitchCache.class.getSimpleName()));

        //non support annotations
        if (m.namedAnnotation != null)
            throw new IncorrectSignatureException(String.format(method + shouldNoHaveAnnotation, m.methodName, Named.class.getSimpleName()));

        if (m.singletonAnn != null)
            throw new IncorrectSignatureException(String.format(method + shouldNoHaveAnnotation, m.methodName, Singleton.class.getSimpleName()));

        if (!Objects.equals(m.methodName, "<init>")) {
            if (Objects.equals(m.returnType, TypeName.VOID) || m.returnType.isPrimitive())
                throw new IncorrectSignatureException(String.format(method + shouldProvideNonPrimitiveObjects, m.methodName));

            for (FieldDetail f : m.args)
                if (f.type.isPrimitive())
                    throw new IncorrectSignatureException(String.format(method + shouldNoHavePrimitiveArguments, m.methodName));
        }
    }

    private static void checkClassNoHaveFields(ClassDetail cl) {
        if (!cl.fields.isEmpty())
            throw new IncorrectSignatureException(String.format(dependencyClass + shouldNoHaveFields, cl.className));
    }

}
