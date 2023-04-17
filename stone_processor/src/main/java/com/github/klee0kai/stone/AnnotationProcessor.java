package com.github.klee0kai.stone;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.codegen.ComponentBuilder;
import com.github.klee0kai.stone.codegen.ModuleBuilder;
import com.github.klee0kai.stone.codegen.ModuleCacheControlInterfaceBuilder;
import com.github.klee0kai.stone.codegen.ModuleFactoryBuilder;
import com.github.klee0kai.stone.codegen.helpers.AllClassesHelper;
import com.github.klee0kai.stone.exceptions.ComponentsMethodPurposeNotDetectedException;
import com.github.klee0kai.stone.exceptions.CreateStoneComponentException;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;

import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.LinkedList;
import java.util.List;
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
        List<ClassName> allQualifiers = new LinkedList<>();
        for (Element componentElement : roundEnv.getElementsAnnotatedWith(Component.class)) {
            ClassDetail component = ClassDetail.of((TypeElement) componentElement);
            allClassesHelper.deepExtractGcAnnotations(component);

            for (ClassDetail componentParentCl : component.getAllParents(false)) {
                if (componentParentCl.componentAnn != null) {
                    allQualifiers.addAll(componentParentCl.componentAnn.qualifiers);
                }
            }
        }

        for (Element ownerElement : roundEnv.getElementsAnnotatedWith(Module.class)) {
            ClassDetail module = ClassDetail.of((TypeElement) ownerElement);

            ModuleFactoryBuilder factoryBuilder = ModuleFactoryBuilder.fromModule(module, allQualifiers);
            factoryBuilder.buildAndWrite();

            ModuleCacheControlInterfaceBuilder moduleCacheControlInterfaceBuilder = ModuleCacheControlInterfaceBuilder.from(factoryBuilder, allQualifiers);
            moduleCacheControlInterfaceBuilder.buildAndWrite();


            ModuleBuilder moduleBuilder = ModuleBuilder.from(factoryBuilder, allQualifiers);
            moduleBuilder.buildAndWrite();
        }

        //create components
        for (Element ownerElement : roundEnv.getElementsAnnotatedWith(Component.class)) {
            try {
                ClassDetail component = ClassDetail.of((TypeElement) ownerElement);
                ComponentBuilder componentBuilder = ComponentBuilder.from(component);

                for (ClassName wrappedProvider : component.componentAnn.wrapperProviders) {
                    ClassDetail wrappedProviderCl = allClassesHelper.findForType(wrappedProvider);
                    if (wrappedProviderCl != null) componentBuilder.addProvideWrapperField(wrappedProviderCl);
                }

                for (MethodDetail m : component.getAllMethods(false, false, "<init>")) {
                    if (allClassesHelper.iComponentClassDetails.findMethod(m, false) != null)
                        continue;

                    if (isModuleProvideMethod(m)) {
                        componentBuilder.provideModuleMethod(m.methodName, allClassesHelper.findForType(m.returnType));
                    } else if (isObjectProvideMethod(m)) {
                        componentBuilder.provideObjMethod(m);
                    } else if (isBindInstanceAndProvideMethod(m) || isBindInstanceMethod(m)) {
                        componentBuilder.bindInstanceMethod(m);
                    } else if (isGcMethod(m)) {
                        componentBuilder.gcMethod(m);
                    } else if (isSwitchCacheMethod(m)) {
                        componentBuilder.switchRefMethod(m);
                    } else if (isInjectMethod(m)) {
                        componentBuilder.injectMethod(m);
                    } else if (isProtectInjectedMethod(m)) {
                        componentBuilder.protectInjectedMethod(
                                m.methodName,
                                allClassesHelper.findForType(m.args.get(0).type),
                                m.protectInjectedAnnotation.timeMillis
                        );
                    } else if (component.isInterfaceClass() || m.isAbstract()) {
                        //non implemented method
                        throw new ComponentsMethodPurposeNotDetectedException(component.className, m);
                    }
                }
                componentBuilder.buildAndWrite();

            } catch (Exception e) {
                throw new CreateStoneComponentException(ownerElement, e);
            }

        }
        return false;
    }
}

