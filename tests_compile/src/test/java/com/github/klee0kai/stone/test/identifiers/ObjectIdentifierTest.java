package com.github.klee0kai.stone.test.identifiers;

import com.github.klee0kai.stone.AnnotationProcessor;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.Test;

public class ObjectIdentifierTest {

    @Test
    void doubleModuleTest() {
        AnnotationProcessor annotationProcessor = new AnnotationProcessor();
        Compilation compilation = Compiler.javac()
                .withProcessors(annotationProcessor)
                .compile(JavaFileObjects.forResource("identifiers/ObjectIdentifiersError.java"));

        CompilationSubject.assertThat(compilation).failed();

        CompilationSubject.assertThat(compilation)
                .hadErrorContainingMatch("should not have Object identifiers");
    }

}
