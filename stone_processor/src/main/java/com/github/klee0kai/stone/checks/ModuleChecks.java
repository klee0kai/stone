package com.github.klee0kai.stone.checks;

import com.github.klee0kai.stone.exceptions.IncorrectSignatureException;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.FieldDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.model.annotations.*;
import com.squareup.javapoet.TypeName;

import java.util.Objects;

import static com.github.klee0kai.stone.exceptions.ExceptionStringBuilder.createErrorMes;

public class ModuleChecks {

    public static void checkModuleClass(ClassDetail cl) {
        try {
            checkClassAnnotations(cl);
            checkModuleClassNoHaveFields(cl);
            for (MethodDetail m : cl.getAllMethods(false, true))
                checkModuleMethodSignature(m);
        } catch (Exception e) {
            throw new IncorrectSignatureException(
                    createErrorMes()
                            .moduleClass(cl.className.toString())
                            .hasIncorrectSignature()
                            .build(),
                    e
            );
        }
    }


    public static void checkModuleMethodSignature(MethodDetail m) {
        IAnnotation prohibitedAnn = m.anyAnnotation(InjectAnn.class, ProtectInjectedAnn.class, SwitchCacheAnn.class, QualifierAnn.class, SingletonAnn.class);
        if (prohibitedAnn != null) {
            throw new IncorrectSignatureException(
                    createErrorMes()
                            .method(m.methodName)
                            .shouldNoHaveAnnotation(prohibitedAnn.originalAnn().getSimpleName())
                            .build()
            );
        }

        if (!Objects.equals(m.methodName, "<init>")) {
            if (Objects.equals(m.returnType, TypeName.VOID) || m.returnType.isPrimitive()) {
                throw new IncorrectSignatureException(
                        createErrorMes()
                                .method(m.methodName)
                                .shouldProvideNonPrimitiveObjects()
                                .build()
                );
            }

            for (FieldDetail f : m.args)
                if (f.type.isPrimitive()) {
                    throw new IncorrectSignatureException(
                            createErrorMes()
                                    .method(m.methodName)
                                    .shouldNoHavePrimitiveArguments()
                                    .build()
                    );
                }
        }
    }

    public static void checkModuleClassNoHaveFields(ClassDetail cl) {
        if (!cl.fields.isEmpty()) {
            throw new IncorrectSignatureException(
                    createErrorMes()
                            .dependencyClass(cl.className.toString())
                            .shouldNoHaveFields()
                            .build()
            );
        }
    }


    private static void checkClassAnnotations(ClassDetail cl) {
        IAnnotation prohibitedAnn = cl.anyAnnotation(ComponentAnn.class, DependenciesAnn.class, WrapperCreatorsAnn.class);
        if (prohibitedAnn != null) {
            throw new IncorrectSignatureException(
                    createErrorMes()
                            .moduleClass(cl.className.toString())
                            .shouldNoHaveAnnotation(prohibitedAnn.originalAnn().getSimpleName())
                            .build()
            );
        }
    }


}
