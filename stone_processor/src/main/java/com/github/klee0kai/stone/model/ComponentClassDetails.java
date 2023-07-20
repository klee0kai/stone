package com.github.klee0kai.stone.model;


import com.github.klee0kai.stone.checks.ComponentMethods;
import com.github.klee0kai.stone.closed.types.StListUtils;
import com.github.klee0kai.stone.helpers.invokecall.ModulesGraph;
import com.github.klee0kai.stone.model.annotations.ComponentAnn;
import com.github.klee0kai.stone.model.annotations.ModuleAnn;
import com.squareup.javapoet.ClassName;

import javax.lang.model.element.TypeElement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.github.klee0kai.stone.AnnotationProcessor.allClassesHelper;
import static com.github.klee0kai.stone.checks.ComponentMethods.isDepsProvide;
import static com.github.klee0kai.stone.checks.ComponentMethods.isModuleProvideMethod;
import static com.github.klee0kai.stone.codegen.ComponentBuilder.hiddenModuleMethodName;
import static com.github.klee0kai.stone.utils.StoneNamingUtils.genCacheControlInterfaceModuleNameMirror;
import static com.github.klee0kai.stone.utils.StoneNamingUtils.genHiddenModuleNameMirror;

/**
 * Collected component's class details of compile type or type specs.
 * Collect the information you need in one place
 */
public class ComponentClassDetails extends ClassDetail {

    public final ModulesGraph modulesGraph = new ModulesGraph();
    public final Set<ClassName> qualifiers = new HashSet<>();
    public ClassDetail hiddenModule = null;
    public ClassName hiddenModuleCacheControlInterface = null;

    public ComponentClassDetails(TypeElement owner) {
        super(owner);
        collectComponentInfo();
    }

    public ComponentClassDetails(ClassDetail cp) {
        super(cp);
        collectComponentInfo();
    }


    private void collectComponentInfo() {
        for (ClassDetail componentParentCl : getAllParents(false)) {
            ComponentAnn parentCompAnn = componentParentCl.ann(ComponentAnn.class);
            if (parentCompAnn != null) qualifiers.addAll(parentCompAnn.qualifiers);
        }
        modulesGraph.allQualifiers.addAll(qualifiers);

        for (MethodDetail m : getAllMethods(false, false, "<init>")) {
            if (allClassesHelper.iComponentClassDetails.findMethod(m, false) != null)
                continue;

            if (isModuleProvideMethod(m)) {
                ClassDetail moduleCl = allClassesHelper.findForType(m.returnType);
                modulesGraph.collectFromModule(m, moduleCl);
            } else if (isDepsProvide(m)) {
                ClassDetail moduleCl = allClassesHelper.findForType(m.returnType);
                modulesGraph.collectFromModule(m, moduleCl);
            }
        }
        if (superClass != null && superClass.hasAnnotations(ComponentAnn.class)) {
            superClass = new ComponentClassDetails(superClass);
        }
        for (int i = 0; i < interfaces.size(); i++) {
            if (interfaces.get(i).hasAnnotations(ComponentAnn.class)) {
                interfaces.set(i, new ComponentClassDetails(interfaces.get(i)));
            }
        }
        genHiddenModule();
    }

    private void genHiddenModule() {
        if (hiddenModule != null) return;
        List<MethodDetail> hiddenBindInstanceMethods = StListUtils.filter(
                getAllMethods(false, false, "<init>"),
                (i, m) -> {
                    boolean isBindAndProvide = ComponentMethods.isBindInstanceMethod(m) == ComponentMethods.BindInstanceType.BindInstanceAndProvide;
                    boolean noInModules = isBindAndProvide && modulesGraph.codeProvideType(m.methodName, m.returnType, m.qualifierAnns) == null;
                    return isBindAndProvide && noInModules;
                });

        hiddenModule = new ClassDetail(genHiddenModuleNameMirror(className));
        hiddenModule.addAnnotation(new ModuleAnn());
        hiddenModule.methods.addAll(hiddenBindInstanceMethods);
        hiddenModuleCacheControlInterface = genCacheControlInterfaceModuleNameMirror(hiddenModule.className);


        for (ClassDetail p : getAllParents(false)) {
            if (p == this || !(p instanceof ComponentClassDetails)) continue;
            ComponentClassDetails parent = (ComponentClassDetails) p;
            hiddenModule.interfaces.add(new ClassDetail(parent.hiddenModule));
        }

        modulesGraph.collectFromModule(
                MethodDetail.simpleName(hiddenModuleMethodName),
                hiddenModule
        );
    }


}
