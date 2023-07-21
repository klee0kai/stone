package com.github.klee0kai.stone.test.common;

import com.github.klee0kai.stone.AnnotationProcessor;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.Test;

public class MothingToProvideTest {

    @Test
    void doubleModuleTest() {
        AnnotationProcessor annotationProcessor = new AnnotationProcessor();
        Compilation compilation = Compiler.javac()
                .withProcessors(annotationProcessor)
                .compile(JavaFileObjects.forResource("common/NothingToProvideError.java"));

        CompilationSubject.assertThat(compilation).failed();

        CompilationSubject.assertThat(compilation)
                .hadErrorContaining("Error provide type java.lang.Object");
        CompilationSubject.assertThat(compilation)
                .hadErrorContaining(" Error to implement method: 'provideObject'");
    }

}
