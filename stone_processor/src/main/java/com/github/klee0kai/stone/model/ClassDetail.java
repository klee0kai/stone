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
import javax.lang.model.type.TypeMirror;
import java.util.*;

public class ClassDetail implements Cloneable {

    public ClassName classType;

    public ComponentAnnotation componentAnn;
    public ModuleAnnotation moduleAnn;

    public List<MethodDetail> methods = new LinkedList<>();

    public List<ClassName> superTypes = new LinkedList<>();
    public ClassDetail superClass = null;
    public List<ClassDetail> interfaces = new LinkedList<>();

    public boolean isInterfaceClass = false;


    public static ClassDetail of(TypeElement owner) {
        ClassDetail classDetail = new ClassDetail();
        List<? extends Element> children = owner.getEnclosedElements();
        classDetail.classType = ClassNameUtils.typeOf(owner.getQualifiedName().toString());
        classDetail.componentAnn = ComponentAnnotation.of(AnnotationMirrorUtil.findAnnotationMirror(owner, Component.class));
        classDetail.moduleAnn = ModuleAnnotation.of(AnnotationMirrorUtil.findAnnotationMirror(owner, Module.class));
        classDetail.isInterfaceClass = owner.getKind().isInterface();

        for (Element el : owner.getEnclosedElements()) {
            if (!(el instanceof ExecutableElement))
                continue;
            classDetail.methods.add(MethodDetail.of((ExecutableElement) el));
        }
        if (owner.getSuperclass() instanceof DeclaredType) {
            if (((DeclaredType) owner.getSuperclass()).asElement() instanceof TypeElement)
                classDetail.superClass = ClassDetail.of((TypeElement) (((DeclaredType) owner.getSuperclass()).asElement()));
        }

        for (TypeMirror tp : owner.getInterfaces()) {
            if (tp instanceof DeclaredType && ((DeclaredType) tp).asElement() instanceof TypeElement)
                classDetail.interfaces.add(ClassDetail.of((TypeElement) ((DeclaredType) tp).asElement()));
        }

        return classDetail;
    }

    public List<MethodDetail> getAllMethods(boolean includeObjectMethods) {
        if (!includeObjectMethods && classType.equals(ClassName.OBJECT))
            return Collections.emptyList();
        LinkedList<MethodDetail> allMethods = new LinkedList<>(this.methods);
        if (superClass != null)
            allMethods.addAll(superClass.getAllMethods(includeObjectMethods));

        LinkedList<MethodDetail> outMethods = new LinkedList<>();
        for (MethodDetail m : allMethods) {
            boolean exist = false;
            for (MethodDetail exitMethod : outMethods) {
                if (exist |= exitMethod.isSameMethod(m))
                    break;
            }
            if (!exist)
                outMethods.add(m);
        }
        return outMethods;
    }

    public boolean haveMethod(MethodDetail methodDetail, boolean checkSuperclass) {
        for (MethodDetail m : methods) {
            if (m.isSameMethod(methodDetail))
                return true;
        }
        if (checkSuperclass && superClass != null && superClass.haveMethod(methodDetail, true))
            return true;
        for (ClassDetail cl : interfaces) {
            if (cl.haveMethod(methodDetail, true))
                return true;
        }
        return false;
    }

    public ClassDetail findInterfaceOverride(MethodDetail m) {
        if (superClass != null && superClass.haveMethod(m, true))
            return superClass;
        for (ClassDetail cl : interfaces) {
            if (cl.haveMethod(m, true))
                return cl;
        }
        return null;
    }

    public int superClassesDeep(boolean includeObject) {
        if (!includeObject && classType.equals(ClassName.OBJECT))
            return 0;
        if (superClass != null)
            return superClass.superClassesDeep(includeObject) + 1;
        return 0;
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
