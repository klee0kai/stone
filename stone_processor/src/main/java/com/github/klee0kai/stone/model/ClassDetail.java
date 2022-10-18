package com.github.klee0kai.stone.model;

import com.github.klee0kai.stone.annotations.Component;
import com.github.klee0kai.stone.annotations.Module;
import com.github.klee0kai.stone.utils.AnnotationMirrorUtil;
import com.github.klee0kai.stone.utils.ClassNameUtils;
import com.squareup.javapoet.ClassName;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import java.lang.reflect.Type;
import java.util.*;

public class ClassDetail implements Cloneable {

    public ClassName classType;


    public ComponentAnnotation componentAnn;
    public ModuleAnnotation moduleAnn;

    public List<MethodDetail> methods = new LinkedList<>();

    public List<ClassName> superTypes = new LinkedList<>();

    public boolean isInterfaceClass = false;


    public static ClassDetail of(TypeElement owner) {
        ClassDetail classDetail = new ClassDetail();
        List<? extends Element> children = owner.getEnclosedElements();
        classDetail.classType = ClassNameUtils.typeOf(owner.getQualifiedName().toString());
        classDetail.componentAnn = ComponentAnnotation.of(AnnotationMirrorUtil.findAnnotationMirror(owner, Component.class));
        classDetail.moduleAnn = ModuleAnnotation.of(AnnotationMirrorUtil.findAnnotationMirror(owner, Module.class));
        classDetail.isInterfaceClass = owner.getKind().isInterface();
        List<MethodDetail> methods = getMethodsForClass(owner);
        for (MethodDetail m : methods) {
            boolean exist = false;
            for (MethodDetail exitMethod : classDetail.methods) {
                if (exist |= exitMethod.isSameMethod(m))
                    break;
            }
            if (!exist)
                classDetail.methods.add(m);
        }

        return classDetail;
    }

    private static List<MethodDetail> getMethodsForClass(TypeElement owner) {
        if (owner.getQualifiedName().toString().equals("java.lang.Object"))
            return Collections.emptyList();
        LinkedList<MethodDetail> methods = new LinkedList<>();
        for (Element el : owner.getEnclosedElements()) {
            if (!(el instanceof ExecutableElement))
                continue;
            methods.add(MethodDetail.of((ExecutableElement) el));
        }
        if (owner.getSuperclass() instanceof DeclaredType) {
            if (((DeclaredType) owner.getSuperclass()).asElement() instanceof TypeElement)
                methods.addAll(getMethodsForClass((TypeElement) (((DeclaredType) owner.getSuperclass()).asElement())));
        }
        return methods;
    }


    @Override
    public ClassDetail clone() throws CloneNotSupportedException {
        return (ClassDetail) super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassDetail that = (ClassDetail) o;
        return isInterfaceClass == that.isInterfaceClass && Objects.equals(classType, that.classType) && Objects.equals(componentAnn, that.componentAnn) && Objects.equals(moduleAnn, that.moduleAnn) && Objects.equals(methods, that.methods) && Objects.equals(superTypes, that.superTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classType, componentAnn, moduleAnn, methods, superTypes, isInterfaceClass);
    }
}
