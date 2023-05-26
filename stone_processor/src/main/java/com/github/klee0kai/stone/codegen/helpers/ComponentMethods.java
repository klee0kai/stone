package com.github.klee0kai.stone.codegen.helpers;

import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.FieldDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.model.annotations.ComponentAnn;
import com.github.klee0kai.stone.model.annotations.DependenciesAnn;
import com.github.klee0kai.stone.model.annotations.ModuleAnn;
import com.github.klee0kai.stone.utils.ClassNameUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.Objects;

import static com.github.klee0kai.stone.AnnotationProcessor.allClassesHelper;

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
        if (m.hasAnyAnnotation() || m.args.isEmpty() || m.returnType != TypeName.VOID) return false;
        for (FieldDetail f : m.args) {
            ClassDetail initClass = allClassesHelper.findForType(f.type);
            if (Objects.equals(ClassNameUtils.rawTypeOf(f.type), ClassName.get(Class.class))) {
                TypeName variant = ((ParameterizedTypeName) f.type).typeArguments.get(0);
                variant = ClassNameUtils.rawTypeOf(variant);
                ClassDetail clVariant = allClassesHelper.findForType(variant);
                if (clVariant != null) initClass = clVariant;
            }
            if (initClass == null) return false;
            if (!initClass.hasAnyAnnotation(ComponentAnn.class, ModuleAnn.class, DependenciesAnn.class))
                return false;
        }
        return true;
    }

    public static boolean isBindInstanceAndProvideMethod(MethodDetail m) {
        return m.bindInstanceAnn != null
                && m.args.size() == 1
                && !m.args.get(0).type.isPrimitive()
                && !m.args.get(0).type.isBoxedPrimitive()
                && Objects.equals(m.returnType, m.args.get(0).type)
                && m.protectInjectedAnn == null
                && m.provideAnn == null
                && m.switchCacheAnn == null;
    }

    public static boolean isBindInstanceMethod(MethodDetail m) {
        return m.bindInstanceAnn != null
                && m.args.size() == 1
                && !m.args.get(0).type.isPrimitive()
                && !m.args.get(0).type.isBoxedPrimitive()
                && m.returnType == TypeName.VOID
                && m.protectInjectedAnn == null
                && m.provideAnn == null
                && m.switchCacheAnn == null
                && m.gcScopeAnnotations.isEmpty();
    }

    public static boolean isGcMethod(MethodDetail m) {
        return !m.gcScopeAnnotations.isEmpty()
                && m.returnType == TypeName.VOID
                && m.protectInjectedAnn == null
                && m.provideAnn == null
                && m.bindInstanceAnn == null
                && m.switchCacheAnn == null;
    }

    public static boolean isSwitchCacheMethod(MethodDetail m) {
        return m.returnType == TypeName.VOID
                && m.switchCacheAnn != null
                && !m.gcScopeAnnotations.isEmpty()
                && (m.args == null || m.args.isEmpty())
                && m.provideAnn == null
                && m.bindInstanceAnn == null;
    }

    public static boolean isInjectMethod(MethodDetail m) {
        boolean injectMethod = m.returnType == TypeName.VOID && m.args != null && m.args.size() >= 1;
        injectMethod &= m.protectInjectedAnn == null
                && m.provideAnn == null
                && m.bindInstanceAnn == null
                && m.switchCacheAnn == null;
        TypeName typeName = injectMethod ? m.args.get(0).type : null;
        injectMethod &= typeName != null
                && allClassesHelper.iComponentClassDetails.findMethod(m, true) == null;
        return injectMethod;
    }

    public static boolean isProtectInjectedMethod(MethodDetail m) {
        boolean protectInjectMethod = m.returnType == TypeName.VOID && m.args != null && m.args.size() == 1
                && m.protectInjectedAnn != null;
        protectInjectMethod &= m.provideAnn == null
                && m.bindInstanceAnn == null
                && m.switchCacheAnn == null;
        TypeName typeName = protectInjectMethod ? m.args.get(0).type : null;
        protectInjectMethod &= typeName != null
                && allClassesHelper.iComponentClassDetails.findMethod(m, true) == null;
        return protectInjectMethod;
    }

    private static boolean isProvideMethod(MethodDetail m) {
        return !m.returnType.isPrimitive()
                && !m.returnType.isBoxedPrimitive()
                && m.returnType != TypeName.VOID
                && m.protectInjectedAnn == null
                && m.provideAnn == null
                && m.bindInstanceAnn == null
                && m.switchCacheAnn == null;
    }


}
