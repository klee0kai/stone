package com.github.klee0kai.stone.utils;

import com.github.klee0kai.stone.AnnotationProcessor;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;


/**
 * Generate java files util
 */
public class CodeFileUtil {

    /**
     * Generate class java file.
     *
     * @param packageName package of type
     * @param spec        type specifications
     */
    public static void writeToJavaFile(String packageName, TypeSpec spec) {
        try {
            JavaFile javaFile =
                    JavaFile.builder(packageName, spec)
                            .addFileComment("Generated by Stone Library\n")
                            .addFileComment("Project " + AnnotationProcessor.PROJECT_URL + "\n")
                            .addFileComment("Copyright (c) 2022 Andrey Kuzubov")
                            .addStaticImport(ClassName.get(Provide.CacheType.class), "*")
                            .build();
            javaFile.writeTo(AnnotationProcessor.env.getFiler());
        } catch (IOException e) {
            //ignore
        }
    }

}

