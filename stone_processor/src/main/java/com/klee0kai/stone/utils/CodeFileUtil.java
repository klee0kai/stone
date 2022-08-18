package com.klee0kai.stone.utils;

import com.klee0kai.stone.AnnotationProcessor;
import com.klee0kai.stone.annotations.Item;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;

public class CodeFileUtil {

    public static void writeToJavaFile(ClassName cl, TypeSpec spec) {
        try {
            JavaFile javaFile =
                    JavaFile.builder(cl.packageName(), spec)
                            .addFileComment("Generated by Stone Library\n")
                            .addFileComment("Copyright (c) 2022 Andrey Kuzubov\n")
                            .addStaticImport(ClassName.get(Item.CacheType.class), "*")
                            .build();
            javaFile.writeTo(AnnotationProcessor.env.getFiler());
        } catch (IOException e) {
//            throw new RuntimeException(e);
        }
    }

}

