package com.github.klee0kai.stone.codegen.helpers;

import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.squareup.javapoet.TypeName;

import java.util.Objects;

import static com.github.klee0kai.stone.AnnotationProcessor.allClassesHelper;

public class ComponentMethods {

    public static boolean isModuleProvideMethod(MethodDetail m) {
        boolean isProvideMethod = isProvideMethod(m);
        ClassDetail providingClDetails = isProvideMethod ? allClassesHelper.findForType(m.returnType) : null;
        return providingClDetails != null
                && providingClDetails.moduleAnn != null
                && m.args.isEmpty();
    }

    public static boolean isDepsProvide(MethodDetail m) {
        boolean isProvideMethod = isProvideMethod(m);
        ClassDetail providingClDetails = isProvideMethod ? allClassesHelper.findForType(m.returnType) : null;
        return providingClDetails != null
                && providingClDetails.dependenciesAnn != null
                && m.args.isEmpty();
    }


    public static boolean isObjectProvideMethod(MethodDetail m) {
        return isProvideMethod(m) && !isModuleProvideMethod(m) && !isDepsProvide(m);
    }

    public static boolean isInitModuleMethod(MethodDetail m) {
        if (m.hasAnyAnnotation() || m.args.size() != 1 || m.returnType != TypeName.VOID) return false;
        ClassDetail initClass = allClassesHelper.findForType(m.args.get(0).type);
        return initClass.moduleAnn != null;
    }

    public static boolean isDepsInitMethod(MethodDetail m) {
        if (m.hasAnyAnnotation() || m.args.size() != 1 || m.returnType != TypeName.VOID) return false;
        ClassDetail initClass = allClassesHelper.findForType(m.args.get(0).type);
        return initClass.dependenciesAnn != null;
    }

    public static boolean isBindInstanceAndProvideMethod(MethodDetail m) {
        return m.bindInstanceAnnotation != null
                && m.args.size() == 1
                && !m.args.get(0).type.isPrimitive()
                && !m.args.get(0).type.isBoxedPrimitive()
                && Objects.equals(m.returnType, m.args.get(0).type)
                && m.protectInjectedAnnotation == null
                && m.provideAnnotation == null
                && m.switchCacheAnnotation == null;
    }

    public static boolean isBindInstanceMethod(MethodDetail m) {
        return m.bindInstanceAnnotation != null
                && m.args.size() == 1
                && !m.args.get(0).type.isPrimitive()
                && !m.args.get(0).type.isBoxedPrimitive()
                && m.returnType == TypeName.VOID
                && m.protectInjectedAnnotation == null
                && m.provideAnnotation == null
                && m.switchCacheAnnotation == null
                && m.gcScopeAnnotations.isEmpty();
    }

    public static boolean isGcMethod(MethodDetail m) {
        return !m.gcScopeAnnotations.isEmpty()
                && m.returnType == TypeName.VOID
                && m.protectInjectedAnnotation == null
                && m.provideAnnotation == null
                && m.bindInstanceAnnotation == null
                && m.switchCacheAnnotation == null;
    }

    public static boolean isSwitchCacheMethod(MethodDetail m) {
        return m.returnType == TypeName.VOID
                && m.switchCacheAnnotation != null
                && !m.gcScopeAnnotations.isEmpty()
                && (m.args == null || m.args.isEmpty())
                && m.provideAnnotation == null
                && m.bindInstanceAnnotation == null;
    }

    public static boolean isInjectMethod(MethodDetail m) {
        boolean injectMethod = m.returnType == TypeName.VOID && m.args != null && m.args.size() >= 1;
        injectMethod &= m.protectInjectedAnnotation == null
                && m.provideAnnotation == null
                && m.bindInstanceAnnotation == null
                && m.switchCacheAnnotation == null;
        TypeName typeName = injectMethod ? m.args.get(0).type : null;
        injectMethod &= typeName != null
                && allClassesHelper.iComponentClassDetails.findMethod(m, true) == null;
        return injectMethod;
    }

    public static boolean isProtectInjectedMethod(MethodDetail m) {
        boolean protectInjectMethod = m.returnType == TypeName.VOID && m.args != null && m.args.size() == 1
                && m.protectInjectedAnnotation != null;
        protectInjectMethod &= m.provideAnnotation == null
                && m.bindInstanceAnnotation == null
                && m.switchCacheAnnotation == null;
        TypeName typeName = protectInjectMethod ? m.args.get(0).type : null;
        protectInjectMethod &= typeName != null
                && allClassesHelper.iComponentClassDetails.findMethod(m, true) == null;
        return protectInjectMethod;
    }

    private static boolean isProvideMethod(MethodDetail m) {
        return !m.returnType.isPrimitive()
                && !m.returnType.isBoxedPrimitive()
                && m.returnType != TypeName.VOID
                && m.protectInjectedAnnotation == null
                && m.provideAnnotation == null
                && m.bindInstanceAnnotation == null
                && m.switchCacheAnnotation == null;
    }


}
