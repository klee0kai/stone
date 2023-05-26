package com.github.klee0kai.stone.checks;

import com.github.klee0kai.stone.exceptions.IncorrectSignatureException;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.FieldDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.model.annotations.*;
import com.squareup.javapoet.TypeName;

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
        IAnnotation prohibitedAnn = m.anyAnnotation(InjectAnn.class, ProtectInjectedAnn.class, SwitchCacheAnn.class, NamedAnn.class, SingletonAnn.class);
        if (prohibitedAnn != null)
            throw new IncorrectSignatureException(String.format(method + shouldNoHaveAnnotation, m.methodName, prohibitedAnn.originalAnn().getSimpleName()));

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
