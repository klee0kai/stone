package com.github.klee0kai.stone.test.module;

import com.github.klee0kai.stone.AnnotationProcessor;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;

public class ModuleTests {

    @Test
    void incorrectAnnotationTest() {
        AnnotationProcessor annotationProcessor = new AnnotationProcessor();
        JavaFileObject file = JavaFileObjects.forResource("module/IncorrectAnnotationError.java");
        Compilation compilation = Compiler.javac()
                .withProcessors(annotationProcessor)
                .compile(file);

        CompilationSubject.assertThat(compilation).failed();

        CompilationSubject.assertThat(compilation)
                .hadErrorContainingMatch("CarInjectModule .*should not have @Dependencies annotation")
                .inFile(file)
                .onLine(16);
    }

    @Test
    void noFieldsTest() {
        AnnotationProcessor annotationProcessor = new AnnotationProcessor();
        JavaFileObject file = JavaFileObjects.forResource("module/NoFieldsError.java");
        Compilation compilation = Compiler.javac()
                .withProcessors(annotationProcessor)
                .compile(file);

        CompilationSubject.assertThat(compilation).failed();

        CompilationSubject.assertThat(compilation)
                .hadErrorContainingMatch("CarInjectModule .*should not have fields")
                .inFile(file)
                .onLine(16);
    }

    @Test
    void noInjectMethodTest() {
        AnnotationProcessor annotationProcessor = new AnnotationProcessor();
        JavaFileObject file = JavaFileObjects.forResource("module/NoInjectError.java");
        Compilation compilation = Compiler.javac()
                .withProcessors(annotationProcessor)
                .compile(file);

        CompilationSubject.assertThat(compilation).failed();

        CompilationSubject.assertThat(compilation)
                .hadErrorContainingMatch("method 'someProvide' should not have @Inject annotation")
                .inFile(file)
                .onLine(21);
    }


    @Test
    void provideThemSelvesTest() {
        AnnotationProcessor annotationProcessor = new AnnotationProcessor();
        JavaFileObject file = JavaFileObjects.forResource("module/ProvideThemSelvesError.java");
        Compilation compilation = Compiler.javac()
                .withProcessors(annotationProcessor)
                .compile(file);

        CompilationSubject.assertThat(compilation).failed();

        CompilationSubject.assertThat(compilation)
                .hadErrorContainingMatch("Error provide type .*String.*Recursive providing detected")
                .inFile(file)
                .onLine(11);
    }

}
