package com.github.klee0kai.stone.model;


import com.github.klee0kai.stone.helpers.invokecall.ModulesGraph;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.TypeElement;

/**
 * Collected component's class details of compile type or type specs.
 * Collect the information you need in one place
 */
public class ComponentClassDetails extends ClassDetail {

    public final ModulesGraph modulesGraph = new ModulesGraph();

    public ComponentClassDetails(TypeElement owner) {
        super(owner);
//        methods
    }

    public ComponentClassDetails(String packageName, TypeSpec owner) {
        super(packageName, owner);
    }


}
