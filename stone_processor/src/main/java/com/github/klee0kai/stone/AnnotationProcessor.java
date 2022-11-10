package com.github.klee0kai.stone;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.component.GcScopeAnnotation;
import com.github.klee0kai.stone.annotations.component.Inject;
import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.codegen.ComponentBuilder;
import com.github.klee0kai.stone.codegen.ModuleBuilder;
import com.github.klee0kai.stone.codegen.ModuleFactoryBuilder;
import com.github.klee0kai.stone.codegen.helpers.AllClassesHelper;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.TypeName;

import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.*;

@AutoService(Processor.class)
@SupportedAnnotationTypes({
        "*"
})
public class AnnotationProcessor extends AbstractProcessor {

    public static final String PROJECT_URL = "https://github.com/klee0kai/stone";
    public static final AllClassesHelper allClassesHelper = new AllClassesHelper();


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
        for (Element ownerElement : roundEnv.getElementsAnnotatedWith(GcScopeAnnotation.class)) {
            ClassDetail gcScopeAnn = ClassDetail.of((TypeElement) ownerElement);
            allClassesHelper.addGcScopeAnnotation(gcScopeAnn);
        }
        for (Element ownerElement : roundEnv.getElementsAnnotatedWith(Module.class)) {
            ClassDetail module = ClassDetail.of((TypeElement) ownerElement);

            ModuleFactoryBuilder factoryBuilder = ModuleFactoryBuilder.fromModule(module);
            factoryBuilder.writeTo(env.getFiler());

            ModuleBuilder moduleBuilder = ModuleBuilder.from(factoryBuilder);
            moduleBuilder.writeTo(env.getFiler());

            allClassesHelper.addModule(module);
        }
        for (Element injectField : roundEnv.getElementsAnnotatedWith(Inject.class)) {
            Element owner = injectField.getEnclosingElement();
            ClassDetail cl = ClassDetail.of((TypeElement) owner);

            allClassesHelper.addInjectClass(cl);
        }


        //create components
        for (Element ownerElement : roundEnv.getElementsAnnotatedWith(Component.class)) {
            ClassDetail component = ClassDetail.of((TypeElement) ownerElement);

            ComponentBuilder componentBuilder = ComponentBuilder.from(component);

            // implement as module method
            for (MethodDetail m : component.getAllMethods(false, "<init>")) {
                boolean provideModuleMethod = !m.returnType.isPrimitive() && !m.returnType.isBoxedPrimitive() && m.returnType != TypeName.VOID;
                if (provideModuleMethod)
                    componentBuilder.provideModuleMethod(m.methodName, allClassesHelper.findModule(m.returnType));
            }

            // gc methods
            for (MethodDetail m : component.getAllMethods(false, "<init>")) {
                boolean isGcMethod = !m.gcScopeAnnotations.isEmpty();
                isGcMethod &= m.returnType == TypeName.VOID;
                if (isGcMethod) componentBuilder.gcMethod(m.methodName, m.gcScopeAnnotations);
            }


            //  implement as inject method
            for (MethodDetail m : component.getAllMethods(false, "<init>")) {
                boolean injectMethod = m.returnType == TypeName.VOID && m.args != null && m.args.size() == 1;
                if (injectMethod) {
                    TypeName typeName = m.args.get(0).type;
                    ClassDetail injCl = allClassesHelper.findInjectCls(typeName);
                    if (injCl != null) componentBuilder.injectMethod(m.methodName, injCl);
                }

            }

            componentBuilder.writeTo(env.getFiler());
        }
        return false;
    }
}

