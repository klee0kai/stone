package com.github.klee0kai.stone.checks;

import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.stone.exceptions.IncorrectSignatureException;
import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.model.annotations.BindInstanceAnn;
import com.github.klee0kai.stone.model.annotations.ProvideAnn;

import static com.github.klee0kai.stone.exceptions.ExceptionStringBuilder.createErrorMes;

public class ModuleMethods {

    public static boolean isProvideCachedObject(MethodDetail m) {
        if (m.hasAnyAnnotation(BindInstanceAnn.class)) return false;
        if (isProvideFactoryObject(m)) return false;

        if (!m.hasOnlyAnnotations(true, ProvideAnn.class)) {
            throw new IncorrectSignatureException(
                    createErrorMes()
                            .method(m.methodName)
                            .hasIncorrectSignature()
                            .shouldHaveOnlyAnnotations(Provide.class.getSimpleName())
                            .build()
            );
        }

        return true;
    }

    public static boolean isProvideFactoryObject(MethodDetail m) {
        if (m.hasAnyAnnotation(BindInstanceAnn.class)) return false;
        ProvideAnn ann = m.ann(ProvideAnn.class);
        if (ann == null || ann.cacheType != Provide.CacheType.Factory) return false;

        if (!m.hasOnlyAnnotations(true, ProvideAnn.class)) {
            throw new IncorrectSignatureException(
                    createErrorMes()
                            .method(m.methodName)
                            .hasIncorrectSignature()
                            .shouldHaveOnlyAnnotations(Provide.class.getSimpleName())
                            .build()
            );
        }

        return true;
    }

    public static boolean isBindInstanceMethod(MethodDetail m) {
        if (!m.hasAnyAnnotation(BindInstanceAnn.class)) return false;

        if (!m.hasOnlyAnnotations(true, BindInstanceAnn.class)) {
            throw new IncorrectSignatureException(
                    createErrorMes()
                            .method(m.methodName)
                            .hasIncorrectSignature()
                            .shouldHaveOnlyAnnotations(Provide.class.getSimpleName())
                            .build()
            );
        }
        return true;
    }

}
