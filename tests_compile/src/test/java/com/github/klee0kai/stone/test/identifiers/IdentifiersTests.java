package com.github.klee0kai.stone.test.identifiers;

import com.github.klee0kai.stone.AnnotationProcessor;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;

public class IdentifiersTests {

    @Test
    void objectIdentifierTest() {
        AnnotationProcessor annotationProcessor = new AnnotationProcessor();
        JavaFileObject file = JavaFileObjects.forResource("identifiers/ObjectIdentifiersError.java");
        Compilation compilation = Compiler.javac()
                .withProcessors(annotationProcessor)
                .compile(file);

        CompilationSubject.assertThat(compilation).failed();

        CompilationSubject.assertThat(compilation)
                .hadErrorContainingMatch("should not have Object identifiers")
                .inFile(file)
                .onLine(7);
    }

    @Test
    void primitiveIdentifierTest() {
        AnnotationProcessor annotationProcessor = new AnnotationProcessor();
        JavaFileObject file = JavaFileObjects.forResource("identifiers/PrimitiveIdentifiersError.java");
        Compilation compilation = Compiler.javac()
                .withProcessors(annotationProcessor)
                .compile(file);

        CompilationSubject.assertThat(compilation).failed();

        CompilationSubject.assertThat(compilation)
                .hadErrorContainingMatch("Primitive types non supported for Component's identifiers")
                .inFile(file)
                .onLine(7);
    }


    @Test
    void provideIdentifierTest() {
        AnnotationProcessor annotationProcessor = new AnnotationProcessor();
        JavaFileObject file = JavaFileObjects.forResource("identifiers/ProvideIdentifierError.java");
        Compilation compilation = Compiler.javac()
                .withProcessors(annotationProcessor)
                .compile(file);

        CompilationSubject.assertThat(compilation).failed();

        CompilationSubject.assertThat(compilation)
                .hadErrorContainingMatch("method 'sayHelloProvide' Should no provide identifier.*String")
                .inFile(file)
                .onLine(11);
    }

}
