package com.github.klee0kai.stone.checks;

import com.github.klee0kai.stone.annotations.wrappers.WrappersCreator;
import com.github.klee0kai.stone.exceptions.IncorrectSignatureException;
import com.github.klee0kai.stone.helpers.wrap.WrapHelper;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.model.annotations.*;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.github.klee0kai.stone.AnnotationProcessor.allClassesHelper;
import static com.github.klee0kai.stone.checks.WrappersCreatorChecks.checkWrapperClass;
import static com.github.klee0kai.stone.exceptions.ExceptionStringBuilder.createErrorMes;

public class ComponentChecks {

    private final WrapHelper wrapHelper;

    public ComponentChecks(WrapHelper wrapHelper) {
        this.wrapHelper = wrapHelper;
    }

    public void checkComponentClass(ClassDetail cl) {
        try {
            checkClassAnnotations(cl);
            checkClassNoHaveFields(cl);
            for (MethodDetail m : cl.getAllMethods(false, true))
                checkMethodSignature(m);
            checkNoModuleDoubles(cl);

        } catch (Exception e) {
            throw new IncorrectSignatureException(
                    createErrorMes()
                            .componentsClass(cl.className.toString())
                            .hasIncorrectSignature()
                            .build(),
                    e,
                    cl.sourceEl
            );
        }
    }

    private void checkClassAnnotations(ClassDetail cl) {
        if (cl.hasAnyAnnotation(WrapperCreatorsAnn.class)) {
            throw new IncorrectSignatureException(
                    createErrorMes()
                            .componentsClass(cl.className.toString())
                            .shouldNoHaveAnnotation(WrappersCreator.class.toString())
                            .build()
            );
        }

        for (ClassName q : cl.ann(ComponentAnn.class).identifiers) {
            if (q.isPrimitive()) {
                throw new IncorrectSignatureException(
                        createErrorMes()
                                .componentsClass(cl.className.toString())
                                .shouldNoHaveIdentifiers("primitive")
                                .build()
                );
            }

            if (Objects.equals(q, ClassName.OBJECT)) {
                throw new IncorrectSignatureException(
                        createErrorMes()
                                .componentsClass(cl.className.toString())
                                .shouldNoHaveIdentifiers("Object")
                                .build()
                );
            }
        }

        for (ClassName wr : cl.ann(ComponentAnn.class).wrapperProviders) {
            ClassDetail wrCl = allClassesHelper.findForType(wr);
            checkWrapperClass(wrCl);
        }
    }

    private void checkNoModuleDoubles(ClassDetail cl) {
        Set<MethodDetail> moduleProvideMethods = new HashSet<>();
        for (MethodDetail m : cl.getAllMethods(false, true)) {
            if (m.hasAnyAnnotation(ModuleOriginFactoryAnn.class)) continue;
            ClassDetail module = allClassesHelper.findForType(m.returnType);
            if (module != null && module.hasAnyAnnotation(ModuleAnn.class, DependenciesAnn.class)) {
                for (MethodDetail moduleMethod : moduleProvideMethods) {
                    if (moduleMethod.isSameMethod(m)) {
                        // overrided method
                        break;
                    }
                    if (moduleMethod.returnType.equals(m.returnType)) {
                        throw new IncorrectSignatureException(
                                createErrorMes()
                                        .componentsClass(cl.className.toString())
                                        .shouldHaveOnlySingleModuleMethod(((ClassName) m.returnType).simpleName())
                                        .build(),
                                m.sourceEl
                        );
                    }
                }

                for (ClassDetail ignored : module.getAllParents(false)) {
                    if (module.hasAnyAnnotation(ModuleAnn.class, DependenciesAnn.class))
                        moduleProvideMethods.add(m);
                }
            }
        }
    }

    private void checkMethodSignature(MethodDetail m) {
        IAnnotation prohibitedAnn = m.anyAnnotation(ProvideAnn.class, QualifierAnn.class, SingletonAnn.class);
        if (prohibitedAnn != null) {
            throw new IncorrectSignatureException(
                    createErrorMes()
                            .method(m.methodName)
                            .shouldNoHaveAnnotation(prohibitedAnn.originalAnn().getSimpleName())
                            .build(),
                    m.sourceEl
            );
        }

        Set<TypeName> identifiers = new HashSet<>(allClassesHelper.allIdentifiers);
        if (identifiers.contains(wrapHelper.nonWrappedType(m.returnType))) {
            throw new IncorrectSignatureException(
                    createErrorMes()
                            .method(m.methodName)
                            .shouldNoProvideIdentifierType(m.returnType.toString())
                            .build(),
                    m.sourceEl
            );
        }
    }

    private static void checkClassNoHaveFields(ClassDetail cl) {
        if (!cl.fields.isEmpty()) {
            throw new IncorrectSignatureException(
                    createErrorMes()
                            .componentsClass(cl.className.toString())
                            .shouldNoHaveFields()
                            .build(),
                    cl.sourceEl
            );
        }
    }


}
