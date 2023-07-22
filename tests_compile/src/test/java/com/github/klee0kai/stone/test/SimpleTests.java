package com.github.klee0kai.stone.test;

import com.github.klee0kai.stone.AnnotationProcessor;
import com.github.klee0kai.stone._hidden_.types.ListUtils;
import com.google.common.collect.ImmutableList;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleTests {

    @Test
    void simpleTest() {
        AnnotationProcessor annotationProcessor = new AnnotationProcessor();
        Compilation compilation = Compiler.javac()
                .withProcessors(annotationProcessor)
                .compile(JavaFileObjects.forResource("SimpleCarComponent.java"));

        CompilationSubject.assertThat(compilation).succeeded();

        ImmutableList<JavaFileObject> generatedFiles = compilation.generatedSourceFiles();
        assertEquals(6, generatedFiles.size(), "1 StoneComponent, 1 Module Factory, 1 Stone Module, 1 CacheControl interface of module, 1 Hidden module factory, 1 hidden module");
        assertEquals(1, ListUtils.filter(generatedFiles, (i, it) -> it.getName().contains("CarInjectComponentStoneComponent")).size());
    }

}
