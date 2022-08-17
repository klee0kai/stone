package com.klee0kai.stone;

import com.google.auto.service.AutoService;
import com.klee0kai.stone.codegen.ComponentGen;
import com.klee0kai.stone.codegen.ModuleGen;

import javax.annotation.processing.*;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes({
        "com.klee0kai.stone.annotations.Component",
        "com.klee0kai.stone.annotations.Module",
        "com.klee0kai.stone.annotations.Item"
})
public class AnnotationProcessor extends AbstractProcessor {

    public static final String PROJECT_URL = "https://github.com/klee0kai/stone";

    public static final ModuleGen moduleGen = new ModuleGen();
    public static final ComponentGen componentGen = new ComponentGen();

    public static ProcessingEnvironment env;
    public static Messager messager;


    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        AnnotationProcessor.env = env;
        messager = env.getMessager();
        moduleGen.init(env);
        componentGen.init(env);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        return false;
    }
}

