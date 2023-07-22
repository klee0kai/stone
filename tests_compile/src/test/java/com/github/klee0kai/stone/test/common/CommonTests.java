package com.github.klee0kai.stone.test.common;

import com.github.klee0kai.stone.AnnotationProcessor;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;

public class CommonTests {

    @Test
    void doubleModuleTest() {
        AnnotationProcessor annotationProcessor = new AnnotationProcessor();
        JavaFileObject file = JavaFileObjects.forResource("common/DoubleModuleError.java");
        Compilation compilation = Compiler.javac()
                .withProcessors(annotationProcessor)
                .compile(file);

        CompilationSubject.assertThat(compilation).failed();

        CompilationSubject.assertThat(compilation)
                .hadErrorContaining("CarInjectModule has duplicate")
                .inFile(file)
                .onLine(11);
    }


    @Test
    void nothingToProvideTest() {
        AnnotationProcessor annotationProcessor = new AnnotationProcessor();
        JavaFileObject file = JavaFileObjects.forResource("common/NothingToProvideError.java");
        Compilation compilation = Compiler.javac()
                .withProcessors(annotationProcessor)
                .compile(file);

        CompilationSubject.assertThat(compilation).failed();

        CompilationSubject.assertThat(compilation)
                .hadErrorContainingMatch("Error to implement method: 'provideObject'.*\n.*Error provide type.*Object")
                .inFile(file)
                .onLine(12);
    }


    @Test
    void noConstructorTest() {
        AnnotationProcessor annotationProcessor = new AnnotationProcessor();
        JavaFileObject file = JavaFileObjects.forResource("common/NoConstructorError.java");
        Compilation compilation = Compiler.javac()
                .withProcessors(annotationProcessor)
                .compile(file);

        CompilationSubject.assertThat(compilation).failed();

        CompilationSubject.assertThat(compilation)
                .hadErrorContainingMatch("No found public constructor for class:.*SomeObject with args:.*Integer")
                .inFile(file)
                .onLine(22);
    }

}
