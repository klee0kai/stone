package com.github.klee0kai.stone.test.component;

import com.github.klee0kai.stone.AnnotationProcessor;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;

public class ComponentTests {

    @Test
    void methodAnnotationTest() {
        AnnotationProcessor annotationProcessor = new AnnotationProcessor();
        JavaFileObject file = JavaFileObjects.forResource("component/MethodAnnotationErrors.java");
        Compilation compilation = Compiler.javac()
                .withProcessors(annotationProcessor)
                .compile(file);

        CompilationSubject.assertThat(compilation).failed();

        CompilationSubject.assertThat(compilation)
                .hadErrorContaining("method 'module' should not have @Provide annotation")
                .inFile(file)
                .onLine(11);
    }


    @Test
    void noFieldsTest() {
        AnnotationProcessor annotationProcessor = new AnnotationProcessor();
        JavaFileObject file = JavaFileObjects.forResource("component/NoFieldsComponent.java");
        Compilation compilation = Compiler.javac()
                .withProcessors(annotationProcessor)
                .compile(file);

        CompilationSubject.assertThat(compilation).failed();

        CompilationSubject.assertThat(compilation)
                .hadErrorContaining("should not have fields")
                .inFile(file)
                .onLine(7);
    }

    @Test
    void noRunGcTest() {
        AnnotationProcessor annotationProcessor = new AnnotationProcessor();
        JavaFileObject file = JavaFileObjects.forResource("component/NoRunGcError.java");
        Compilation compilation = Compiler.javac()
                .withProcessors(annotationProcessor)
                .compile(file);

        CompilationSubject.assertThat(compilation).failed();

        CompilationSubject.assertThat(compilation)
                .hadErrorContainingMatch("What is purpose for Method 'gcAll'")
                .inFile(file)
                .onLine(13);
    }


    @Test
    void noWrapperCreatorTest() {
        AnnotationProcessor annotationProcessor = new AnnotationProcessor();
        JavaFileObject file = JavaFileObjects.forResource("component/NoWrapperCreatorError.java");
        Compilation compilation = Compiler.javac()
                .withProcessors(annotationProcessor)
                .compile(file);

        CompilationSubject.assertThat(compilation).failed();

        CompilationSubject.assertThat(compilation)
                .hadErrorContainingMatch("should not have .*WrappersCreator")
                .inFile(file)
                .onLine(9);
    }


}
