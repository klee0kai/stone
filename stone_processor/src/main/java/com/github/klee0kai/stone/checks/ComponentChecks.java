package com.github.klee0kai.stone.checks;

import com.github.klee0kai.stone.AnnotationProcessor;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.stone.annotations.wrappers.WrappersCreator;
import com.github.klee0kai.stone.exceptions.IncorrectSignatureException;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.squareup.javapoet.ClassName;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Objects;

import static com.github.klee0kai.stone.exceptions.StoneExceptionStrings.*;

public class ComponentChecks {

    public static void checkComponentClass(ClassDetail cl) {
        try {
            checkClassAnnotations(cl);
            checkClassNoHaveFields(cl);
            for (MethodDetail m : cl.getAllMethods(false, true))
                checkMethodSignature(m);
        } catch (Exception e) {
            throw new IncorrectSignatureException(String.format(componentsClass + hasIncorrectSignature, cl.className), e);
        }
    }


    private static void checkClassAnnotations(ClassDetail cl) {
        if (cl.wrapperCreatorsAnn != null)
            throw new IncorrectSignatureException(String.format(componentsClass + shouldNoHaveAnnotation, cl.className, WrappersCreator.class));

        for (ClassName q : cl.componentAnn.qualifiers) {
            if (q.isPrimitive())
                throw new IncorrectSignatureException(String.format(componentsClass + shouldNoHaveQualifier, cl.className, "primitive"));

            if (Objects.equals(q, ClassName.OBJECT))
                throw new IncorrectSignatureException(String.format(componentsClass + shouldNoHaveQualifier, cl.className, "Object"));
        }

        for (ClassName wr : cl.componentAnn.wrapperProviders) {
            ClassDetail wrCl = AnnotationProcessor.allClassesHelper.findForType(wr);
            WrappersCreatorChecks.checkWrapperClass(wrCl);
        }

    }

    private static void checkMethodSignature(MethodDetail m) {
        if (m.provideAnnotation != null)
            throw new IncorrectSignatureException(String.format(method + shouldNoHaveAnnotation, m.methodName, Provide.class.getSimpleName()));

        //non support annotations
        if (m.namedAnnotation != null)
            throw new IncorrectSignatureException(String.format(method + shouldNoHaveAnnotation, m.methodName, Named.class.getSimpleName()));

        if (m.singletonAnnotation != null)
            throw new IncorrectSignatureException(String.format(method + shouldNoHaveAnnotation, m.methodName, Singleton.class.getSimpleName()));
    }

    private static void checkClassNoHaveFields(ClassDetail cl) {
        if (!cl.fields.isEmpty())
            throw new IncorrectSignatureException(String.format(componentsClass + shouldNoHaveFields, cl.className));
    }

}
