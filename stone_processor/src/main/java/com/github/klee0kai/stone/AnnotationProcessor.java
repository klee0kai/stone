package com.github.klee0kai.stone;

import com.github.klee0kai.stone.annotations.Component;
import com.github.klee0kai.stone.annotations.Module;
import com.github.klee0kai.stone.codegen.ComponentBuilder;
import com.github.klee0kai.stone.codegen.ModuleBuilder;
import com.github.klee0kai.stone.codegen.ModuleFactoryBuilder;
import com.github.klee0kai.stone.model.ClassDetail;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.LinkedList;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes({
        "*"
})
public class AnnotationProcessor extends AbstractProcessor {

    public static final String PROJECT_URL = "https://github.com/klee0kai/stone";

    public static ProcessingEnvironment env;
    public static Messager messager;


    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        AnnotationProcessor.env = env;
        messager = env.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        for (Element ownerElement : roundEnv.getElementsAnnotatedWith(Module.class)) {
            ClassDetail module = ClassDetail.of((TypeElement) ownerElement);

            ModuleFactoryBuilder factoryBuilder = ModuleFactoryBuilder.fromModule(module);
            factoryBuilder.writeTo(env.getFiler());

            ModuleBuilder moduleBuilder = ModuleBuilder.from(factoryBuilder);
            moduleBuilder.writeTo(env.getFiler());
        }

        for (Element ownerElement : roundEnv.getElementsAnnotatedWith(Component.class)) {
            ClassDetail component = ClassDetail.of((TypeElement) ownerElement);

            ComponentBuilder componentBuilder = ComponentBuilder.from(component);
            componentBuilder.writeTo(env.getFiler());
        }
        return false;
    }
}

