package com.github.klee0kai.stone.checks;

import com.github.klee0kai.stone.annotations.wrappers.WrappersCreator;
import com.github.klee0kai.stone.exceptions.IncorrectSignatureException;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.model.annotations.*;
import com.github.klee0kai.stone.types.wrappers.AsyncWrapper;
import com.github.klee0kai.stone.types.wrappers.Wrapper;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.github.klee0kai.stone.AnnotationProcessor.allClassesHelper;
import static com.github.klee0kai.stone.exceptions.ExceptionStringBuilder.createErrorMes;

public class WrappersCreatorChecks {

    public static final ClassName wrapperClName = ClassName.get(Wrapper.class);
    public static final ClassName asyncWrapperClName = ClassName.get(AsyncWrapper.class);

    public static void checkWrapperClass(ClassDetail cl) {
        try {
            checkClassAnnotations(cl);
            checkWrapperCreatorInterface(cl);
            checkWrappingClasses(cl);
        } catch (Exception e) {
            throw new IncorrectSignatureException(
                    createErrorMes()
                            .wrappersCreatorClass(cl.className.toString())
                            .hasIncorrectSignature()
                            .build()
                    , e);
        }
    }

    private static void checkClassAnnotations(ClassDetail cl) {
        IAnnotation prohibitedAnn = cl.anyAnnotation(ComponentAnn.class, DependenciesAnn.class, ModuleAnn.class);
        if (prohibitedAnn != null) {
            throw new IncorrectSignatureException(
                    createErrorMes()
                            .wrappersCreatorClass(cl.className.toString())
                            .shouldNoHaveAnnotation(prohibitedAnn.originalAnn().getSimpleName())
                            .build()
            );
        }
    }

    private static void checkWrapperCreatorInterface(ClassDetail cl) {

        List<TypeName> wrapperClasses = Arrays.asList(wrapperClName, asyncWrapperClName);
        boolean isWrapperCreatorInterface = false;
        for (ClassDetail p : cl.getAllParents(false))
            if (wrapperClasses.contains(p.className)) {
                isWrapperCreatorInterface = true;
                break;
            }
        if (!isWrapperCreatorInterface || !cl.hasAnyAnnotation(WrapperCreatorsAnn.class)) {
            throw new IncorrectSignatureException(
                    createErrorMes()
                            .wrappersCreatorClass(cl.className.toString())
                            .shouldImplementInterface(wrapperClName.canonicalName())
                            .add(" or ")
                            .shouldImplementInterface(asyncWrapperClName.canonicalName())
                            .add(" and ")
                            .shouldHaveAnnotations(WrappersCreator.class.getSimpleName())
                            .build()
            );
        }


        MethodDetail m = cl.findMethod(MethodDetail.constructorMethod(Collections.emptyList()), false);
        if (m == null || !m.modifiers.contains(Modifier.PUBLIC)) {
            throw new IncorrectSignatureException(
                    createErrorMes()
                            .wrappersCreatorClass(cl.className.toString())
                            .shouldHaveConstructorWithoutArgs()
                            .build()
            );
        }
    }

    private static void checkWrappingClasses(ClassDetail cl) {
        for (ClassName wrClName : cl.ann(WrapperCreatorsAnn.class).wrappers) {
            TypeElement wrCl = allClassesHelper.typeElementFor(wrClName);
            if (wrCl.getTypeParameters().size() != 1) {
                throw new IncorrectSignatureException(
                        createErrorMes()
                                .wrapperShouldBeGenericType1(cl.className)
                                .build()
                );
            }
        }
    }

}
