package com.github.klee0kai.stone.codegen.helpers;

import com.github.klee0kai.stone.AnnotationProcessor;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.FieldDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.types.ListUtils;
import com.github.klee0kai.stone.types.lifecycle.IStoneLifeCycleOwner;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.util.*;

public class AllClassesHelper {

    private Elements elements;
    private final Map<TypeName, ClassDetail> injectClasses = new HashMap<>();
    private final Map<TypeName, ClassDetail> modules = new HashMap<>();
    private final Set<TypeName> lifeCycleOwners = new HashSet<>();
    private final Map<String, ClassDetail> gcScopeAnnotations = new HashMap<>();

    public AllClassesHelper() {
    }

    public void init(Elements elements) {
        this.elements = elements;
    }

    public void addModule(ClassDetail module) {
        modules.put(module.className, module);
    }

    public void addGcScopeAnnotation(ClassDetail classDetail) {
        gcScopeAnnotations.put(classDetail.className.toString(), classDetail);
    }

    public void addInjectClass(ClassDetail injCl) {
        if (!injectClasses.containsKey(injCl.className))
            injectClasses.put(injCl.className, injCl);
    }

    public ClassDetail findModule(TypeName moduleType) {
        return modules.getOrDefault(moduleType, null);
    }


    public ClassDetail findInjectCls(TypeName injectCl) {
        return injectClasses.getOrDefault(injectCl, null);
    }

    public ClassDetail findGcScopeAnnotation(String annTypeName) {
        return gcScopeAnnotations.getOrDefault(annTypeName, null);
    }


    public boolean isLifeCycleOwner(TypeName typeName) {
        if (lifeCycleOwners.contains(typeName))
            return true;
        ClassDetail cl = findForType(typeName);
        if (cl.isExtOf(ClassName.get(IStoneLifeCycleOwner.class))) {
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
