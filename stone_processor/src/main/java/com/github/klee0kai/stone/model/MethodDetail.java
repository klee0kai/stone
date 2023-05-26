package com.github.klee0kai.stone.model;

import com.github.klee0kai.stone.AnnotationProcessor;
import com.github.klee0kai.stone.annotations.component.*;
import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.stone.model.annotations.*;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.lang.model.element.*;
import java.lang.annotation.Annotation;
import java.util.*;

public class MethodDetail implements Cloneable {

    public String methodName;

    public TypeName returnType;

    public Set<Modifier> modifiers = Collections.emptySet();

    public ElementKind elementKind;

    public List<FieldDetail> args = new LinkedList<>();

    public ProvideAnn provideAnn;
    public BindInstanceAnn bindInstanceAnn;

    public ProtectInjectedAnn protectInjectedAnn;
    public SwitchCacheAnn switchCacheAnn;

    public InjectAnn injectAnnotation;
    public NamedAnn namedAnnotation;
    public SingletonAnn singletonAnn;


    public LinkedList<TypeName> gcScopeAnnotations = new LinkedList<>();

    public static MethodDetail simpleName(String methodName) {
        MethodDetail methodDetail = new MethodDetail();
        methodDetail.methodName = methodName;
        return methodDetail;
    }

    public static MethodDetail of(ExecutableElement element) {
        MethodDetail methodDetail = new MethodDetail();
        methodDetail.methodName = element.getSimpleName().toString();
        methodDetail.returnType = TypeName.get(element.getReturnType());
        methodDetail.modifiers = element.getModifiers();
        methodDetail.elementKind = element.getKind();
        methodDetail.provideAnn = ProvideAnn.of(element.getAnnotation(Provide.class));
        methodDetail.bindInstanceAnn = BindInstanceAnn.of(element.getAnnotation(BindInstance.class));
        methodDetail.protectInjectedAnn = ProtectInjectedAnn.of(element.getAnnotation(ProtectInjected.class));
        methodDetail.switchCacheAnn = SwitchCacheAnn.of(element.getAnnotation(SwitchCache.class));
        methodDetail.injectAnnotation = InjectAnn.of(element.getAnnotation(Inject.class));
        methodDetail.namedAnnotation = NamedAnn.of(element.getAnnotation(Named.class));
        methodDetail.singletonAnn = SingletonAnn.of(element.getAnnotation(Singleton.class));

        List<Class<? extends Annotation>> scClasses = Arrays.asList(GcAllScope.class, GcWeakScope.class, GcSoftScope.class, GcStrongScope.class);
        for (Class<? extends Annotation> sc : scClasses)
            if (element.getAnnotation(sc) != null)
                methodDetail.gcScopeAnnotations.add(ClassName.get(sc));

        for (AnnotationMirror ann : element.getAnnotationMirrors()) {
            String clName = ann.getAnnotationType().toString();
            ClassDetail annClDet = AnnotationProcessor.allClassesHelper.findGcScopeAnnotation(clName);
            if (annClDet != null) methodDetail.gcScopeAnnotations.add(annClDet.className);
        }
        for (VariableElement v : element.getParameters())
            methodDetail.args.add(FieldDetail.of(v));
        return methodDetail;
    }

    public static MethodDetail of(MethodSpec methodSpec) {
        MethodDetail methodDetail = new MethodDetail();
        methodDetail.methodName = methodSpec.name;
        methodDetail.returnType = methodSpec.returnType;
        methodDetail.modifiers = methodSpec.modifiers;
        methodDetail.args = ListUtils.format(methodSpec.parameters, FieldDetail::of);

        methodDetail.provideAnn = ProvideAnn.findFrom(methodSpec.annotations);
        methodDetail.bindInstanceAnn = BindInstanceAnn.findFrom(methodSpec.annotations);
        methodDetail.protectInjectedAnn = ProtectInjectedAnn.findFrom(methodSpec.annotations);
        methodDetail.switchCacheAnn = SwitchCacheAnn.findFrom(methodSpec.annotations);
        methodDetail.injectAnnotation = InjectAnn.findFrom(methodSpec.annotations);
        methodDetail.namedAnnotation = NamedAnn.findFrom(methodSpec.annotations);
        methodDetail.singletonAnn = SingletonAnn.findFrom(methodSpec.annotations);


        return methodDetail;
    }

    public static MethodDetail simpleGetMethod(String name, TypeName typeName) {
        MethodDetail m = new MethodDetail();
        m.methodName = name;
        m.returnType = typeName;
        return m;
    }

    public static MethodDetail simpleSetMethod(String name, TypeName typeName) {
        MethodDetail m = new MethodDetail();
        m.methodName = name;
        m.args.add(FieldDetail.simple(name, typeName));
        m.returnType = TypeName.VOID;
        return m;
    }

    public static MethodDetail constructorMethod(List<FieldDetail> args) {
        MethodDetail m = new MethodDetail();
        m.methodName = "<init>";
        m.args = args;
        return m;
    }

    public boolean isAbstract() {
        return modifiers.contains(Modifier.ABSTRACT);
    }

    /**
     * methods is same if they have same name and params
     *
     * @param methodDetail
     * @return
     */
    public boolean isSameMethod(MethodDetail methodDetail) {
        if (methodDetail == null
                || !Objects.equals(this.methodName, methodDetail.methodName)
                || ((args == null) != (methodDetail.args == null))
                || args != null && args.size() != methodDetail.args.size()) {
            return false;
        }
        if (args == null)
            return true;
        for (int i = 0; i < args.size(); i++)
            if (!Objects.equals(args.get(i).type, methodDetail.args.get(i).type))
                return false;
        return true;
    }

    public boolean hasAnyAnnotation() {
        return provideAnn != null
                || bindInstanceAnn != null
                || protectInjectedAnn != null
                || switchCacheAnn != null
                || injectAnnotation != null
                || namedAnnotation != null
                || singletonAnn != null
                || !gcScopeAnnotations.isEmpty();
    }

    @Override
    public MethodDetail clone() throws CloneNotSupportedException {
        return (MethodDetail) super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodDetail that = (MethodDetail) o;
        return Objects.equals(methodName, that.methodName) && Objects.equals(returnType, that.returnType) && Objects.equals(args, that.args) && Objects.equals(provideAnn, that.provideAnn) && Objects.equals(bindInstanceAnn, that.bindInstanceAnn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(methodName, returnType, args, provideAnn, bindInstanceAnn);
    }


}
