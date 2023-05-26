package com.github.klee0kai.stone.checks;

import com.github.klee0kai.stone.annotations.wrappers.WrappersCreator;
import com.github.klee0kai.stone.exceptions.IncorrectSignatureException;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.model.annotations.*;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.github.klee0kai.stone.AnnotationProcessor.allClassesHelper;
import static com.github.klee0kai.stone.exceptions.StoneExceptionStrings.*;

public class ComponentChecks {

    public static void checkComponentClass(ClassDetail cl) {
        try {
            checkClassAnnotations(cl);
            checkClassNoHaveFields(cl);
            for (MethodDetail m : cl.getAllMethods(false, true))
                checkMethodSignature(m);
            checkNoModuleDoubles(cl);
        } catch (Exception e) {
            throw new IncorrectSignatureException(String.format(componentsClass + hasIncorrectSignature, cl.className), e);
        }
    }


    private static void checkClassAnnotations(ClassDetail cl) {
        if (cl.hasAnyAnnotation(WrapperCreatorsAnn.class))
            throw new IncorrectSignatureException(String.format(componentsClass + shouldNoHaveAnnotation, cl.className, WrappersCreator.class));

        for (ClassName q : cl.ann(ComponentAnn.class).qualifiers) {
            if (q.isPrimitive())
                throw new IncorrectSignatureException(String.format(componentsClass + shouldNoHaveQualifier, cl.className, "primitive"));

            if (Objects.equals(q, ClassName.OBJECT))
                throw new IncorrectSignatureException(String.format(componentsClass + shouldNoHaveQualifier, cl.className, "Object"));
        }

        for (ClassName wr : cl.ann(ComponentAnn.class).wrapperProviders) {
            ClassDetail wrCl = allClassesHelper.findForType(wr);
            WrappersCreatorChecks.checkWrapperClass(wrCl);
        }
    }

    private static void checkNoModuleDoubles(ClassDetail cl) {
        Set<TypeName> modules = new HashSet<>();
        for (MethodDetail m : cl.getAllMethods(false, true)) {
            ClassDetail module = allClassesHelper.findForType(m.returnType);
            if (module != null && module.hasAnyAnnotation(ModuleAnn.class, DependenciesAnn.class)) {
                if (modules.contains(m.returnType)) {
                    throw new IncorrectSignatureException(
                            String.format(componentsClass + shouldHaveOnlySingleModuleMethod,
                                    cl.className,
                                    ((ClassName) m.returnType).simpleName())
                    );
                }

                for (ClassDetail p : module.getAllParents(false)) {
                    if (module.hasAnyAnnotation(ModuleAnn.class, DependenciesAnn.class))
                        modules.add(m.returnType);
                }
            }
        }
    }

    private static void checkMethodSignature(MethodDetail m) {
        IAnnotation prohibitedAnn = m.anyAnnotation(ProvideAnn.class, NamedAnn.class, SingletonAnn.class);
        if (prohibitedAnn != null)
            throw new IncorrectSignatureException(String.format(method + shouldNoHaveAnnotation, m.methodName, prohibitedAnn.originalAnn().getSimpleName()));
    }

    private static void checkClassNoHaveFields(ClassDetail cl) {
        if (!cl.fields.isEmpty())
            throw new IncorrectSignatureException(String.format(componentsClass + shouldNoHaveFields, cl.className));
    }

}
