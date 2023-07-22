package com.github.klee0kai.stone.checks;

import com.github.klee0kai.stone.exceptions.IncorrectSignatureException;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.model.annotations.ComponentAnn;
import com.github.klee0kai.stone.model.annotations.IAnnotation;
import com.github.klee0kai.stone.model.annotations.ModuleAnn;
import com.github.klee0kai.stone.model.annotations.WrapperCreatorsAnn;

import static com.github.klee0kai.stone.exceptions.ExceptionStringBuilder.createErrorMes;

public class DependencyChecks {

    public static void checkDependencyClass(ClassDetail cl) {
        try {
            checkClassAnnotations(cl);
            ModuleChecks.checkModuleClassNoHaveFields(cl);
            for (MethodDetail m : cl.getAllMethods(false, true))
                ModuleChecks.checkModuleMethodSignature(m);
        } catch (Exception cause) {
            throw new IncorrectSignatureException(
                    createErrorMes()
                            .dependencyClass(cl.className.toString())
                            .hasIncorrectSignature()
                            .build(),
                    cause,
                    cl.sourceEl);
        }
    }

    private static void checkClassAnnotations(ClassDetail cl) {
        IAnnotation prohibitedAnn = cl.anyAnnotation(ComponentAnn.class, ModuleAnn.class, WrapperCreatorsAnn.class);
        if (prohibitedAnn != null) {
            throw new IncorrectSignatureException(
                    createErrorMes()
                            .dependencyClass(cl.className.toString())
                            .shouldNoHaveAnnotation(prohibitedAnn.originalAnn().getSimpleName())
                            .build(),
                    cl.sourceEl
            );
        }
    }

}
