package com.github.klee0kai.stone.checks;

import com.github.klee0kai.stone.exceptions.IncorrectSignatureException;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.FieldDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.model.annotations.*;
import com.squareup.javapoet.TypeName;

import java.util.Objects;

import static com.github.klee0kai.stone.exceptions.ExceptionStringBuilder.createErrorMes;

public class DependencyChecks {

    public static void checkDependencyClass(ClassDetail cl) {
        try {
            checkClassAnnotations(cl);
            checkClassNoHaveFields(cl);
            for (MethodDetail m : cl.getAllMethods(false, true))
                checkMethodSignature(m);
        } catch (Exception e) {
            throw new IncorrectSignatureException(
                    createErrorMes()
                            .dependencyClass(cl.className.toString())
                            .hasIncorrectSignature()
                            .build(),
                    e);
        }
    }

    private static void checkClassAnnotations(ClassDetail cl) {
        IAnnotation prohibitedAnn = cl.anyAnnotation(ComponentAnn.class, ModuleAnn.class, WrapperCreatorsAnn.class);
        if (prohibitedAnn != null) {
            throw new IncorrectSignatureException(
                    createErrorMes()
                            .dependencyClass(cl.className.toString())
                            .shouldNoHaveAnnotation(prohibitedAnn.originalAnn().getSimpleName())
                            .build()
            );
        }
    }


    private static void checkMethodSignature(MethodDetail m) {
        IAnnotation prohibitedAnn = m.anyAnnotation(InjectAnn.class, ProtectInjectedAnn.class, SwitchCacheAnn.class, NamedAnn.class, SingletonAnn.class);
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

    private static void checkClassNoHaveFields(ClassDetail cl) {
        if (!cl.fields.isEmpty()) {
            throw new IncorrectSignatureException(
                    createErrorMes()
                            .dependencyClass(cl.className.toString())
                            .shouldNoHaveFields()
                            .build()
            );
        }
    }

}
