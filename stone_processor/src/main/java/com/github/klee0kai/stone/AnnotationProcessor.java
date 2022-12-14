package com.github.klee0kai.stone;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.component.GcScopeAnnotation;
import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.codegen.ComponentBuilder;
import com.github.klee0kai.stone.codegen.ModuleBuilder;
import com.github.klee0kai.stone.codegen.ModuleFactoryBuilder;
import com.github.klee0kai.stone.codegen.ModuleInterfaceBuilder;
import com.github.klee0kai.stone.codegen.helpers.AllClassesHelper;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;

import javax.annotation.processing.*;
import javax.inject.Scope;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

import static com.github.klee0kai.stone.codegen.helpers.ComponentMethods.*;

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
        for (Element ownerElement : roundEnv.getElementsAnnotatedWith(Scope.class)) {
            ClassDetail gcScopeAnn = ClassDetail.of((TypeElement) ownerElement);
            allClassesHelper.addGcScopeAnnotation(gcScopeAnn);
        }
        for (Element ownerElement : roundEnv.getElementsAnnotatedWith(Module.class)) {
            ClassDetail module = ClassDetail.of((TypeElement) ownerElement);

            ModuleFactoryBuilder factoryBuilder = ModuleFactoryBuilder.fromModule(module);
            factoryBuilder.writeTo(env.getFiler());

            ModuleInterfaceBuilder moduleInterfaceBuilder = ModuleInterfaceBuilder.from(factoryBuilder);
            moduleInterfaceBuilder.writeTo(env.getFiler());


            ModuleBuilder moduleBuilder = ModuleBuilder.from(factoryBuilder);
            moduleBuilder.writeTo(env.getFiler());
        }


        //create components
        for (Element ownerElement : roundEnv.getElementsAnnotatedWith(Component.class)) {
            ClassDetail component = ClassDetail.of((TypeElement) ownerElement);
            ComponentBuilder componentBuilder = ComponentBuilder.from(component);


            for (ClassName wrappedProvider : component.componentAnn.wrapperProviders) {
                ClassDetail cl = allClassesHelper.findForType(wrappedProvider);
                if (cl != null) componentBuilder.addProvideWrapperField(cl);
            }


            for (MethodDetail m : component.getAllMethods(false, "<init>")) {
                if (isModuleProvideMethod(m)) {
                    componentBuilder.provideModuleMethod(m.methodName, allClassesHelper.findForType(m.returnType));
                } else if (isObjectProvideMethod(m)) {
                    componentBuilder.provideObjMethod(m.methodName, m.returnType, m.args);
                } else if (isGcMethod(m)) {
                    componentBuilder.gcMethod(m.methodName, m.gcScopeAnnotations);
                } else if (isSwitchCacheMethod(m)) {
                    componentBuilder.switchRefMethod(m.methodName, m.switchCacheAnnotation, m.gcScopeAnnotations);
                } else if (isInjectMethod(m)) {
                    componentBuilder.injectMethod(m.methodName, m.args);
                } else if (isProtectInjectedMethod(m)) {
                    componentBuilder.protectInjectedMethod(
                            m.methodName,
                            allClassesHelper.findForType(m.args.get(0).type),
                            m.protectInjectedAnnotation.timeMillis
                    );
                }
            }

            componentBuilder.writeTo(env.getFiler());
        }
        return false;
    }
}

