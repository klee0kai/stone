package com.github.klee0kai.stone.model;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.dependencies.Dependencies;
import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.wrappers.WrappersCreator;
import com.github.klee0kai.stone.model.annotations.*;
import com.github.klee0kai.stone.utils.AnnotationMirrorUtil;
import com.github.klee0kai.stone.utils.ClassNameUtils;
import com.squareup.javapoet.*;

import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.*;

import static com.github.klee0kai.stone.AnnotationProcessor.allClassesHelper;

public class ClassDetail implements Cloneable {

    public TypeName className;

    public Set<Modifier> modifiers = Collections.emptySet();

    public TypeKindDetails kind;

    public List<MethodDetail> methods = new LinkedList<>();
    public List<FieldDetail> fields = new LinkedList<>();

    public ClassDetail superClass = null;
    public List<ClassDetail> interfaces = new LinkedList<>();


    // ------- annotations ---------

    private Map<Class<? extends IAnnotation>, IAnnotation> annotations = new HashMap<>();


    public static ClassDetail of(TypeElement owner) {
        ClassDetail classDetail = new ClassDetail();
        classDetail.className = ClassNameUtils.classNameOf(owner.getQualifiedName().toString());
        classDetail.modifiers = owner.getModifiers();
        classDetail.kind = TypeKindDetails.of(owner.getKind());

        classDetail.addAnnotation(ComponentAnn.of(AnnotationMirrorUtil.findAnnotationMirror(owner, Component.class)));
        classDetail.addAnnotation(ModuleAnn.of(AnnotationMirrorUtil.findAnnotationMirror(owner, Module.class)));
        classDetail.addAnnotation(DependenciesAnn.of(AnnotationMirrorUtil.findAnnotationMirror(owner, Dependencies.class)));
        classDetail.addAnnotation(WrapperCreatorsAnn.of(AnnotationMirrorUtil.findAnnotationMirror(owner, WrappersCreator.class)));


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

    /**
     * Annotations not supported
     */
    public static ClassDetail of(String packageName, TypeSpec owner) {
        ClassDetail classDetail = new ClassDetail();
        classDetail.className = ClassName.get(packageName, owner.name);
        classDetail.modifiers = owner.modifiers;
        classDetail.kind = TypeKindDetails.of(owner.kind);

        classDetail.addAnnotation(ComponentAnn.findFrom(owner.annotations));
        classDetail.addAnnotation(ModuleAnn.findFrom(owner.annotations));
        classDetail.addAnnotation(DependenciesAnn.findFrom(owner.annotations));
        classDetail.addAnnotation(WrapperCreatorsAnn.findFrom(owner.annotations));

        for (MethodSpec m : owner.methodSpecs)
            classDetail.methods.add(MethodDetail.of(m));

        for (FieldSpec f : owner.fieldSpecs)
            classDetail.fields.add(FieldDetail.of(f));

        classDetail.superClass = allClassesHelper.findForType(owner.superclass);
        for (TypeName t : owner.superinterfaces)
            classDetail.interfaces.add(allClassesHelper.findForType(t));

        return classDetail;
    }

    /**
     * @param includeObjectMethods
     * @param allowDoubles         for use return type and overriden methods
     * @return
     */
    public List<MethodDetail> getAllMethods(boolean includeObjectMethods, boolean allowDoubles) {
        if (!includeObjectMethods && className.equals(ClassName.OBJECT))
            return Collections.emptyList();
        LinkedList<MethodDetail> allMethods = new LinkedList<>(this.methods);
        if (superClass != null)
            allMethods.addAll(superClass.getAllMethods(includeObjectMethods, false));
        if (interfaces != null) for (ClassDetail interf : interfaces)
            allMethods.addAll(interf.getAllMethods(includeObjectMethods, false));

        LinkedList<MethodDetail> outMethods = new LinkedList<>();
        for (MethodDetail m : allMethods) {
            boolean exist = false;
            for (MethodDetail exitMethod : outMethods) {
                if (exist |= exitMethod.isSameMethod(m))
                    break;
            }
            if (!exist || allowDoubles)
                outMethods.add(m);
        }
        return outMethods;
    }


    public List<FieldDetail> getAllFields() {
        if (className.equals(ClassName.OBJECT))
            return Collections.emptyList();
        LinkedList<FieldDetail> allFields = new LinkedList<>(this.fields);
        if (superClass != null)
            allFields.addAll(superClass.getAllFields());
        if (interfaces != null) for (ClassDetail interf : interfaces)
            allFields.addAll(interf.getAllFields());

        LinkedList<FieldDetail> outFields = new LinkedList<>();
        for (FieldDetail m : allFields) {
            boolean exist = false;
            for (FieldDetail exitMethod : outFields) {
                if (exist |= (Objects.equals(exitMethod.name, m.name)))
                    break;
            }
            if (!exist)
                outFields.add(m);
        }
        return outFields;
    }

    public List<ClassDetail> getAllParents(boolean includeObject) {
        if (!includeObject && className.equals(ClassName.OBJECT))
            return Collections.emptyList();
        List<ClassDetail> parents = new LinkedList<>();
        parents.add(this);
        if (superClass != null)
            parents.addAll(superClass.getAllParents(includeObject));
        if (interfaces != null) for (ClassDetail interf : interfaces)
            parents.addAll(interf.getAllParents(includeObject));
        return parents;
    }

    public List<MethodDetail> getAllMethods(boolean includeObjectMethods, boolean allowDoubles, String... exceptNames) {
        LinkedList<MethodDetail> outMethods = new LinkedList<>();
        for (MethodDetail m : getAllMethods(includeObjectMethods, allowDoubles)) {
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

    public MethodDetail findMethod(MethodDetail methodDetail, boolean checkSuperclass) {
        for (MethodDetail m : methods) {
            if (m.isSameMethod(methodDetail))
                return m;
        }
        if (checkSuperclass) {
            if (superClass != null) {
                MethodDetail m = superClass.findMethod(methodDetail, true);
                if (m != null) return m;
            }
            for (ClassDetail cl : interfaces) {
                MethodDetail m = cl.findMethod(methodDetail, true);
                if (m != null) return m;
            }
        }
        return null;
    }

    public ClassDetail findInterfaceOverride(MethodDetail m) {
        if (superClass != null && superClass.findMethod(m, true) != null)
            return superClass;
        for (ClassDetail cl : interfaces) {
            if (cl.findMethod(m, true) != null)
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
        return kind == TypeKindDetails.INTERFACE;
    }

    public void addAnnotation(IAnnotation annotation) {
        if (annotation != null) annotations.put(annotation.getClass(), annotation);
    }

    public <T> T ann(Class<T> tClass) {
        return (T) annotations.getOrDefault(tClass, null);
    }

    @SafeVarargs
    public final IAnnotation anyAnnotation(Class<? extends IAnnotation>... aClasses) {
        for (Class<? extends IAnnotation> cl : aClasses) {
            IAnnotation ann = annotations.getOrDefault(cl, null);
            if (ann != null) return ann;
        }
        return null;
    }

    @SafeVarargs
    public final boolean hasAnnotations(Class<? extends IAnnotation>... aClasses) {
        for (Class<? extends IAnnotation> cl : aClasses) {
            if (!annotations.containsKey(cl)) return false;
        }
        return true;
    }

    @SafeVarargs
    public final boolean hasAnyAnnotation(Class<? extends IAnnotation>... aClasses) {
        for (Class<? extends IAnnotation> cl : aClasses) {
            if (annotations.containsKey(cl)) return true;
        }
        return false;
    }

    @SafeVarargs
    public final boolean hasOnlyAnnotations(Class<? extends IAnnotation>... aClasses) {
        if (annotations.size() != aClasses.length) return false;
        for (Class<? extends IAnnotation> cl : aClasses) {
            if (!annotations.containsKey(cl)) return false;
        }
        return true;
    }


    @Override
    public ClassDetail clone() throws CloneNotSupportedException {
        return (ClassDetail) super.clone();
    }


}
