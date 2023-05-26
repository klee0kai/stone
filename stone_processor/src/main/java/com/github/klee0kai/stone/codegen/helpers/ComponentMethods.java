package com.github.klee0kai.stone.codegen.helpers;

import com.github.klee0kai.stone.annotations.component.ExtendOf;
import com.github.klee0kai.stone.annotations.component.Init;
import com.github.klee0kai.stone.annotations.component.ProtectInjected;
import com.github.klee0kai.stone.annotations.component.SwitchCache;
import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.stone.exceptions.IncorrectSignatureException;
import com.github.klee0kai.stone.exceptions.StoneException;
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
import static com.github.klee0kai.stone.exceptions.StoneExceptionStrings.*;

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
        StoneException ex = new IncorrectSignatureException(String.format(componentInitMethodSignatureIncorrect, m.methodName, Init.class.getSimpleName()));
        if (!m.hasOnlyAnnotations(false, InitAnn.class)
                || m.args.isEmpty() || m.returnType != TypeName.VOID)
            throw ex;

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
                throw ex;
        }
        checkMethodBusy(m);

        return true;
    }

    public static boolean isExtOfMethod(ClassDetail cl, MethodDetail m) {
        if (!m.hasAnyAnnotation(ExtOfAnn.class)) return false;
        StoneException ex = new IncorrectSignatureException(String.format(componentExtOfMethodSignatureIncorrect, m.methodName, ExtendOf.class.getSimpleName()));
        if (!m.hasOnlyAnnotations(false, ExtOfAnn.class)
                || m.args.size() != 1 || m.returnType != TypeName.VOID)
            throw ex;
        ClassDetail parentComponent = allClassesHelper.findForType(m.args.get(0).type);
        if (!parentComponent.hasOnlyAnnotations(ComponentAnn.class) || !cl.isExtOf(parentComponent.className))
            throw ex;
        checkMethodBusy(m);

        return true;
    }

    public static BindInstanceType isBindInstanceMethod(MethodDetail m) {
        if (!m.hasAnyAnnotation(BindInstanceAnn.class)) return null;
        StoneException ex = new IncorrectSignatureException(String.format(componentBindInstanceMethodSignatureIncorrect, m.methodName, BindInstance.class.getSimpleName()));
        if (!m.hasOnlyAnnotations(true, BindInstanceAnn.class)) throw ex;
        if (m.args.size() != 1) throw ex;
        TypeName typeName = m.args.get(0).type;
        if (typeName.isPrimitive() || typeName.isBoxedPrimitive()) throw ex;
        checkMethodBusy(m);

        if (m.returnType == TypeName.VOID)
            return BindInstanceType.BindInstance;
        if (Objects.equals(m.returnType, m.args.get(0).type))
            return BindInstanceType.BindInstanceAndProvide;

        throw ex;
    }

    public static boolean isGcMethod(MethodDetail m) {
        if (m.gcScopeAnnotations.isEmpty() || !m.hasOnlyAnnotations(true)) return false;
        if (m.returnType != TypeName.VOID)
            throw new IncorrectSignatureException(String.format(componentGCMethodSignatureIncorrect, m.methodName));
        checkMethodBusy(m);

        return true;
    }

    public static boolean isSwitchCacheMethod(MethodDetail m) {
        if (!m.hasAnyAnnotation(SwitchCacheAnn.class)) return false;
        StoneException ex = new IncorrectSignatureException(String.format(componentSwitchCacheMethodSignatureIncorrect, m.methodName, SwitchCache.class.getSimpleName()));
        if (!m.hasOnlyAnnotations(true, SwitchCacheAnn.class)) throw ex;
        if (m.returnType != TypeName.VOID || !m.args.isEmpty()) throw ex;
        checkMethodBusy(m);

        return true;
    }

    public static boolean isInjectMethod(MethodDetail m) {
        if (!m.hasOnlyAnnotations(false) || m.returnType != TypeName.VOID || m.args.size() < 1)
            return false;
        checkMethodBusy(m);

        return true;
    }

    public static boolean isProtectInjectedMethod(MethodDetail m) {
        if (!m.hasAnyAnnotation(ProtectInjectedAnn.class)) return false;
        StoneException ex = new IncorrectSignatureException(String.format(componentProtectInjectedMethodSignatureIncorrect, m.methodName, ProtectInjected.class.getSimpleName()));
        if (!m.hasOnlyAnnotations(false, ProtectInjectedAnn.class) || m.returnType != TypeName.VOID || m.args.size() != 1)
            throw ex;
        TypeName typeName = m.args.get(0).type;
        if (typeName.isPrimitive() || typeName.isBoxedPrimitive())
            throw ex;
        checkMethodBusy(m);

        return true;
    }

    private static boolean isProvideMethod(MethodDetail m) {
        return !m.returnType.isPrimitive()
                && !m.returnType.isBoxedPrimitive()
                && m.returnType != TypeName.VOID
                && m.hasOnlyAnnotations(false);
    }


    private static void checkMethodBusy(MethodDetail m) {
        if (allClassesHelper.iComponentClassDetails.findMethod(m, true) != null)
            throw new IncorrectSignatureException(componentMethodNameBusy);
    }

    public enum BindInstanceType {
        BindInstance,
        BindInstanceAndProvide
    }

}
