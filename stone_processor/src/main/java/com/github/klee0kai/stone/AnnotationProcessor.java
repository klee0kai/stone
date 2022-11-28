package com.github.klee0kai.stone;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.component.GcScopeAnnotation;
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
        allClassesHelper.init(env.getElementUtils());
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
        }


        //create components
        for (Element ownerElement : roundEnv.getElementsAnnotatedWith(Component.class)) {
            ClassDetail component = ClassDetail.of((TypeElement) ownerElement);

            ComponentBuilder componentBuilder = ComponentBuilder.from(component);

            // implement as module method
            for (MethodDetail m : component.getAllMethods(false, "<init>")) {
                boolean provideModuleMethod = !m.returnType.isPrimitive() && !m.returnType.isBoxedPrimitive() && m.returnType != TypeName.VOID;
                provideModuleMethod &= m.protectInjectedAnnotation == null && m.provideAnnotation == null
                        && m.bindInstanceAnnotation == null;
                if (provideModuleMethod)
                    componentBuilder.provideModuleMethod(m.methodName, allClassesHelper.findForType(m.returnType));
            }

            // gc methods
            for (MethodDetail m : component.getAllMethods(false, "<init>")) {
                boolean isGcMethod = !m.gcScopeAnnotations.isEmpty();
                isGcMethod &= m.returnType == TypeName.VOID;
                isGcMethod &= m.protectInjectedAnnotation == null && m.provideAnnotation == null
                        && m.bindInstanceAnnotation == null;

                if (isGcMethod) componentBuilder.gcMethod(m.methodName, m.gcScopeAnnotations);
            }


            //  implement as inject method
            for (MethodDetail m : component.getAllMethods(false, "<init>")) {
                boolean injectMethod = m.returnType == TypeName.VOID && m.args != null && m.args.size() >= 1;
                injectMethod &= m.protectInjectedAnnotation == null && m.provideAnnotation == null
                        && m.bindInstanceAnnotation == null;
                TypeName typeName = injectMethod ? m.args.get(0).type : null;
                injectMethod &= typeName != null && !allClassesHelper.iComponentClassDetails.haveMethod(m, true);
                if (injectMethod) componentBuilder.injectMethod(m.methodName, m.args);
            }

            //  implement protect inject method
            for (MethodDetail m : component.getAllMethods(false, "<init>")) {
                boolean protectInjectMethod = m.returnType == TypeName.VOID && m.args != null && m.args.size() == 1
                        && m.protectInjectedAnnotation != null;
                protectInjectMethod &= m.provideAnnotation == null && m.bindInstanceAnnotation == null;
                TypeName typeName = protectInjectMethod ? m.args.get(0).type : null;
                protectInjectMethod &= typeName != null && !allClassesHelper.iComponentClassDetails.haveMethod(m, true);
                if (protectInjectMethod) {
                    ClassDetail injCl = allClassesHelper.findForType(typeName);
                    if (injCl != null) componentBuilder.protectInjected(m.methodName, injCl,
                            m.protectInjectedAnnotation.timeMillis);
                }
            }

            componentBuilder.writeTo(env.getFiler());
        }
        return false;
    }
}

