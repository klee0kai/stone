package com.github.klee0kai.stone.model;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.model.annotations.ComponentAnnotation;
import com.github.klee0kai.stone.model.annotations.ModuleAnnotation;
import com.github.klee0kai.stone.utils.AnnotationMirrorUtil;
import com.github.klee0kai.stone.utils.ClassNameUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.*;

public class ClassDetail implements Cloneable {

    public ClassName className;

    public Set<Modifier> modifiers = Collections.emptySet();

    public ElementKind elementKind;

    public List<MethodDetail> methods = new LinkedList<>();
    public List<FieldDetail> fields = new LinkedList<>();

    public ClassDetail superClass = null;
    public List<ClassDetail> interfaces = new LinkedList<>();


    // ------- annotations ---------
    public ComponentAnnotation componentAnn;
    public ModuleAnnotation moduleAnn;


    public static ClassDetail of(TypeElement owner) {
        ClassDetail classDetail = new ClassDetail();
        classDetail.className = ClassNameUtils.typeOf(owner.getQualifiedName().toString());
        classDetail.modifiers = owner.getModifiers();
        classDetail.elementKind = owner.getKind();

        classDetail.componentAnn = ComponentAnnotation.of(AnnotationMirrorUtil.findAnnotationMirror(owner, Component.class));
        classDetail.moduleAnn = ModuleAnnotation.of(AnnotationMirrorUtil.findAnnotationMirror(owner, Module.class));


        for (Element el : owner.getEnclosedElements()) {
            if (el instanceof ExecutableElement)
                classDetail.methods.add(MethodDetail.of((ExecutableElement) el));
            else if (el instanceof VariableElement)
                classDetail.fields.add(FieldDetail.of((VariableElement) el));
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
        if (!includeObjectMethods && className.equals(ClassName.OBJECT))
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

    public List<MethodDetail> getAllMethods(boolean includeObjectMethods, String... exceptNames) {
        LinkedList<MethodDetail> outMethods = new LinkedList<>();
        for (MethodDetail m : getAllMethods(includeObjectMethods)) {
            boolean ignore = false;
            if (exceptNames != null)
                for (String ex : exceptNames)
                    if (Objects.equals(ex, m.methodName)) {
                        ignore = true;
                        break;
                    }
            if (!ignore)
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
        if (!includeObject && className.equals(ClassName.OBJECT))
            return 0;
        if (superClass != null)
            return superClass.superClassesDeep(includeObject) + 1;
        return 0;
    }

    public boolean isExtOf(TypeName typeName) {
        if (Objects.equals(typeName, this.className))
            return true;
        if (superClass != null && superClass.isExtOf(typeName))
            return true;
        if (interfaces != null) for (ClassDetail i : interfaces)
            if (i.isExtOf(typeName))
                return true;
        return false;
    }

    public boolean isAbstractClass() {
        return modifiers.contains(Modifier.ABSTRACT);
    }

    public boolean isInterfaceClass() {
        return elementKind.isInterface();
    }

    @Override
    public ClassDetail clone() throws CloneNotSupportedException {
        return (ClassDetail) super.clone();
    }


}
