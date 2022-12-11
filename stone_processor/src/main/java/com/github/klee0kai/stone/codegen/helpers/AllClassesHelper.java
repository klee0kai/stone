package com.github.klee0kai.stone.codegen.helpers;

import com.github.klee0kai.stone.closed.IModule;
import com.github.klee0kai.stone.interfaces.IComponent;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.types.lifecycle.IStoneLifeCycleOwner;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import javax.lang.model.util.Elements;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AllClassesHelper {

    private Elements elements;
    private final Set<TypeName> lifeCycleOwners = new HashSet<>();
    private final Map<String, ClassDetail> gcScopeAnnotations = new HashMap<>();

    public ClassDetail iComponentClassDetails;
    public ClassDetail iModule;
    public ClassDetail iLifeCycleOwner;

    public AllClassesHelper() {
    }

    public void init(Elements elements) {
        this.elements = elements;
        iComponentClassDetails = findForType(ClassName.get(IComponent.class));
        iModule = findForType(ClassName.get(IModule.class));
        iLifeCycleOwner = findForType(ClassName.get(IStoneLifeCycleOwner.class));
    }

    public void addGcScopeAnnotation(ClassDetail classDetail) {
        gcScopeAnnotations.put(classDetail.className.toString(), classDetail);
    }

    public ClassDetail findGcScopeAnnotation(String annTypeName) {
        return gcScopeAnnotations.getOrDefault(annTypeName, null);
    }


    public boolean isLifeCycleOwner(TypeName typeName) {
        if (lifeCycleOwners.contains(typeName))
            return true;
        ClassDetail cl = findForType(typeName);
        if (cl.isExtOf(iLifeCycleOwner.className)) {
            lifeCycleOwners.add(typeName);
            return true;
        }
        return false;
    }

    public ClassDetail findForType(TypeName typeName) {
        if (typeName instanceof ClassName)
            return ClassDetail.of(elements.getTypeElement(((ClassName) typeName).canonicalName()));
        return null;
    }
}
