package com.github.klee0kai.stone.model;


import com.github.klee0kai.stone.helpers.invokecall.ModulesGraph;
import com.github.klee0kai.stone.model.annotations.ComponentAnn;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.TypeElement;
import java.util.HashSet;
import java.util.Set;

import static com.github.klee0kai.stone.AnnotationProcessor.allClassesHelper;
import static com.github.klee0kai.stone.checks.ComponentMethods.isDepsProvide;
import static com.github.klee0kai.stone.checks.ComponentMethods.isModuleProvideMethod;

/**
 * Collected component's class details of compile type or type specs.
 * Collect the information you need in one place
 */
public class ComponentClassDetails extends ClassDetail {

    public final ModulesGraph modulesGraph = new ModulesGraph();
    public final Set<ClassName> qualifiers = new HashSet<>();

    public ComponentClassDetails(TypeElement owner) {
        super(owner);
        collectComponentInfo();

    }

    public ComponentClassDetails(String packageName, TypeSpec owner) {
        super(packageName, owner);
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
    }


}
