package com.github.klee0kai.stone.codegen.helpers;

import com.github.klee0kai.stone.annotations.component.ExtendOf;
import com.github.klee0kai.stone.annotations.component.Init;
import com.github.klee0kai.stone.exceptions.IncorrectSignatureException;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.FieldDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.model.annotations.*;
import com.github.klee0kai.stone.utils.ClassNameUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.Objects;

import static com.github.klee0kai.stone.AnnotationProcessor.allClassesHelper;
import static com.github.klee0kai.stone.exceptions.StoneExceptionStrings.componentExtOfMethodSignatureIncorrect;
import static com.github.klee0kai.stone.exceptions.StoneExceptionStrings.componentInitMethodSignatureIncorrect;

public class ComponentMethods {

    public static boolean isModuleProvideMethod(MethodDetail m) {
        boolean isProvideMethod = isProvideMethod(m);
        ClassDetail providingClDetails = isProvideMethod ? allClassesHelper.findForType(m.returnType) : null;
        return providingClDetails != null
                && providingClDetails.hasAnnotations(ModuleAnn.class)
                && m.args.isEmpty();
    }

    public static boolean isDepsProvide(MethodDetail m) {
        boolean isProvideMethod = isProvideMethod(m);
        ClassDetail providingClDetails = isProvideMethod ? allClassesHelper.findForType(m.returnType) : null;
        return providingClDetails != null
                && providingClDetails.hasAnnotations(DependenciesAnn.class)
                && m.args.isEmpty();
    }


    public static boolean isObjectProvideMethod(MethodDetail m) {
        return isProvideMethod(m) && !isModuleProvideMethod(m) && !isDepsProvide(m);
    }

    public static boolean isInitModuleMethod(MethodDetail m) {
        if (!m.hasAnyAnnotation(InitAnn.class)) return false;
        if (!m.hasOnlyAnnotations(false, InitAnn.class)
                || m.args.isEmpty() || m.returnType != TypeName.VOID)
            throw new IncorrectSignatureException(String.format(componentInitMethodSignatureIncorrect, m.methodName, Init.class.getSimpleName()));

        for (FieldDetail f : m.args) {
            ClassDetail initClass = allClassesHelper.findForType(f.type);
            if (Objects.equals(ClassNameUtils.rawTypeOf(f.type), ClassName.get(Class.class))) {
                TypeName variant = ((ParameterizedTypeName) f.type).typeArguments.get(0);
                variant = ClassNameUtils.rawTypeOf(variant);
                ClassDetail clVariant = allClassesHelper.findForType(variant);
                if (clVariant != null) initClass = clVariant;
            }
            if (initClass == null) return false;
            if (!initClass.hasAnyAnnotation(ModuleAnn.class, DependenciesAnn.class))
                throw new IncorrectSignatureException(String.format(componentInitMethodSignatureIncorrect, m.methodName, Init.class.getSimpleName()));
        }
        return true;
    }

    public static boolean isExtOfMethod(ClassDetail cl, MethodDetail m) {
        if (!m.hasAnyAnnotation(ExtOfAnn.class)) return false;
        if (!m.hasOnlyAnnotations(false, ExtOfAnn.class)
                || m.args.size() != 1 || m.returnType != TypeName.VOID)
            throw new IncorrectSignatureException(String.format(componentExtOfMethodSignatureIncorrect, m.methodName, ExtendOf.class.getSimpleName()));
        ClassDetail parentComponent = allClassesHelper.findForType(m.args.get(0).type);
        if (!parentComponent.hasOnlyAnnotations(ComponentAnn.class) || !cl.isExtOf(parentComponent.className))
            throw new IncorrectSignatureException(String.format(componentExtOfMethodSignatureIncorrect, m.methodName, ExtendOf.class.getSimpleName()));
        return true;
    }

    public static boolean isBindInstanceAndProvideMethod(MethodDetail m) {
        return m.hasOnlyAnnotations(true, BindInstanceAnn.class)
                && m.args.size() == 1
                && !m.args.get(0).type.isPrimitive()
                && !m.args.get(0).type.isBoxedPrimitive()
                && Objects.equals(m.returnType, m.args.get(0).type);
    }

    public static boolean isBindInstanceMethod(MethodDetail m) {
        return m.hasOnlyAnnotations(true, BindInstanceAnn.class)
                && m.args.size() == 1
                && !m.args.get(0).type.isPrimitive()
                && !m.args.get(0).type.isBoxedPrimitive()
                && m.returnType == TypeName.VOID;
    }

    public static boolean isGcMethod(MethodDetail m) {
        return !m.gcScopeAnnotations.isEmpty()
                && m.returnType == TypeName.VOID
                && m.hasOnlyAnnotations(true);
    }

    public static boolean isSwitchCacheMethod(MethodDetail m) {
        return m.returnType == TypeName.VOID
                && m.hasOnlyAnnotations(true, SwitchCacheAnn.class)
                && (m.args == null || m.args.isEmpty());
    }

    public static boolean isInjectMethod(MethodDetail m) {
        boolean injectMethod = m.returnType == TypeName.VOID && m.args != null && m.args.size() >= 1;
        injectMethod &= m.hasOnlyAnnotations(false);
        TypeName typeName = injectMethod ? m.args.get(0).type : null;
        injectMethod &= typeName != null
                && allClassesHelper.iComponentClassDetails.findMethod(m, true) == null;
        return injectMethod;
    }

    public static boolean isProtectInjectedMethod(MethodDetail m) {
        boolean protectInjectMethod = m.returnType == TypeName.VOID && m.args != null && m.args.size() == 1;
        protectInjectMethod &= m.hasOnlyAnnotations(false, ProtectInjectedAnn.class);
        TypeName typeName = protectInjectMethod ? m.args.get(0).type : null;
        protectInjectMethod &= typeName != null
                && allClassesHelper.iComponentClassDetails.findMethod(m, true) == null;
        return protectInjectMethod;
    }

    private static boolean isProvideMethod(MethodDetail m) {
        return !m.returnType.isPrimitive()
                && !m.returnType.isBoxedPrimitive()
                && m.returnType != TypeName.VOID
                && m.hasOnlyAnnotations(false);
    }


}
