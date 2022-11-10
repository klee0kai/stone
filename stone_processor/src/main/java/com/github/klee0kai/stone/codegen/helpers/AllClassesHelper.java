package com.github.klee0kai.stone.codegen.helpers;

import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.TypeElement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AllClassesHelper {


    private final Map<TypeName, ClassDetail> injectClasses = new HashMap<>();
    private final Map<TypeName, ClassDetail> modules = new HashMap<>();
    private final Map<String, ClassDetail> gcScopeAnnotations = new HashMap<>();

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




    public ClassDetail findGcScopeAnnotation(String annTypeName){
        return gcScopeAnnotations.getOrDefault(annTypeName,null);
    }
}
