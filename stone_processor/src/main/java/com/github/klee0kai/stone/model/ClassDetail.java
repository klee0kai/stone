package com.github.klee0kai.stone.model;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.dependencies.Dependencies;
import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.wrappers.WrappersCreator;
import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.stone.model.annotations.*;
import com.github.klee0kai.stone.utils.AnnotationMirrorUtil;
import com.github.klee0kai.stone.utils.ClassNameUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.*;

import static com.github.klee0kai.stone.helpers.SetFieldHelper.capitalized;

/**
 * Collected class details of compile type or type specs.
 * Collect the information you need in one place
 */
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

    public ClassDetail(TypeName className) {
        this.className = className;
    }


    /**
     * Take class details from compile type element
     *
     * @param owner original element
     */
    public ClassDetail(TypeElement owner) {
        className = ClassNameUtils.classNameOf(owner.getQualifiedName().toString());
        modifiers = owner.getModifiers();
        kind = TypeKindDetails.of(owner.getKind());

        addAnnotation(ComponentAnn.of(AnnotationMirrorUtil.findAnnotationMirror(owner, Component.class)));
        addAnnotation(ModuleAnn.of(AnnotationMirrorUtil.findAnnotationMirror(owner, Module.class)));
        addAnnotation(DependenciesAnn.of(AnnotationMirrorUtil.findAnnotationMirror(owner, Dependencies.class)));
        addAnnotation(WrapperCreatorsAnn.of(AnnotationMirrorUtil.findAnnotationMirror(owner, WrappersCreator.class)));

        for (Element el : owner.getEnclosedElements()) {
            if (el instanceof ExecutableElement)
                methods.add(MethodDetail.of((ExecutableElement) el));
            else if (el instanceof VariableElement) {
                String getAnnotationsElName = "get" + capitalized(el.getSimpleName().toString()) + "$annotations";
                Element getAnnotationsEl = ListUtils.first(owner.getEnclosedElements(), (i, it) ->
                        Objects.equals(it.getSimpleName().toString(), getAnnotationsElName)
                );
                fields.add(FieldDetail.of((VariableElement) el, getAnnotationsEl));
            }
        }
        if (owner.getSuperclass() instanceof DeclaredType) {
            if (((DeclaredType) owner.getSuperclass()).asElement() instanceof TypeElement)
                superClass = new ClassDetail((TypeElement) (((DeclaredType) owner.getSuperclass()).asElement()));
        }

        for (TypeMirror tp : owner.getInterfaces()) {
            if (tp instanceof DeclaredType && ((DeclaredType) tp).asElement() instanceof TypeElement)
                interfaces.add(new ClassDetail((TypeElement) ((DeclaredType) tp).asElement()));
        }
    }

    public ClassDetail(ClassDetail cp) {
        this.className = cp.className;
        this.modifiers = cp.modifiers;
        this.kind = cp.kind;
        this.methods = cp.methods;
        this.fields = cp.fields;
        this.superClass = cp.superClass;
        this.interfaces = cp.interfaces;
        this.annotations = cp.annotations;
    }

    /**
     * List all methods of this class include parent classes and interfaces
     *
     * @param includeObjectMethods return object's methods (equals, hashCode and etc)
     * @param allowDoubles         true if we want to check return types of overridden methods
     * @param exceptNames          filter names by except list
     * @return list of all methods
     */
    public List<MethodDetail> getAllMethods(boolean includeObjectMethods, boolean allowDoubles, String... exceptNames) {
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
            if (exist && !allowDoubles) continue;
            boolean ignore = false;
            if (exceptNames != null)
                for (String ex : exceptNames)
                    if (Objects.equals(ex, m.methodName)) {
                        ignore = true;
                        break;
                    }
            if (ignore) continue;
            outMethods.add(m);
        }
        return outMethods;
    }

    /**
     * List all fields of this class include parent classes
     *
     * @return list of all fields
     */
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

    /**
     * List all parents of class: classes and interfaces.
     *
     * @param includeObject return also java object's class
     * @return list for all parents
     */
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


    /**
     * Find method by method details.
     * Compare by name and argument's list.
     *
     * @param methodDetail    excepting method
     * @param checkSuperclass search in superclasses
     * @return found method details
     */
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

    /**
     * Check for superclasses
     *
     * @param typeName excepting superclass
     * @return true if class has superclass
     */
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

    /**
     * @return true if is abstract class
     */
    public boolean isAbstractClass() {
        return modifiers.contains(Modifier.ABSTRACT);
    }

    /**
     * @return true if is interface class
     */
    public boolean isInterfaceClass() {
        return kind == TypeKindDetails.INTERFACE;
    }

    /**
     * Add annotation to class
     *
     * @param annotation new annotation
     */
    public void addAnnotation(IAnnotation annotation) {
        if (annotation != null) annotations.put(annotation.getClass(), annotation);
    }

    /**
     * Get annotation by type
     *
     * @param tClass class name of annotation
     * @param <T>    type of annotation
     * @return found annotation
     */
    public <T> T ann(Class<T> tClass) {
        return (T) annotations.getOrDefault(tClass, null);
    }

    /**
     * Check is any annotation exist
     *
     * @param aClasses list of annotation types
     * @return found first annotation from list
     */
    @SafeVarargs
    public final IAnnotation anyAnnotation(Class<? extends IAnnotation>... aClasses) {
        for (Class<? extends IAnnotation> cl : aClasses) {
            IAnnotation ann = annotations.getOrDefault(cl, null);
            if (ann != null) return ann;
        }
        return null;
    }

    /**
     * Check is list of annotations exist
     *
     * @param aClasses list of annotation types
     * @return true if all annotations from list exist
     */
    @SafeVarargs
    public final boolean hasAnnotations(Class<? extends IAnnotation>... aClasses) {
        for (Class<? extends IAnnotation> cl : aClasses) {
            if (!annotations.containsKey(cl)) return false;
        }
        return true;
    }

    /**
     * Check is any of annotation exist
     *
     * @param aClasses list of annotation types
     * @return true if any annotation from list exist
     */
    @SafeVarargs
    public final boolean hasAnyAnnotation(Class<? extends IAnnotation>... aClasses) {
        for (Class<? extends IAnnotation> cl : aClasses) {
            if (annotations.containsKey(cl)) return true;
        }
        return false;
    }

    /**
     * Check class have annotations exact as annotation list
     *
     * @param aClasses list of annotation types
     * @return true if class has exact same annotations list
     */
    @SafeVarargs
    public final boolean hasOnlyAnnotations(Class<? extends IAnnotation>... aClasses) {
        if (annotations.size() != aClasses.length) return false;
        for (Class<? extends IAnnotation> cl : aClasses) {
            if (!annotations.containsKey(cl)) return false;
        }
        return true;
    }

    /**
     * copy class details
     */
    @Override
    public ClassDetail clone() throws CloneNotSupportedException {
        return (ClassDetail) super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassDetail that = (ClassDetail) o;
        return Objects.equals(className, that.className) && Objects.equals(modifiers, that.modifiers) && kind == that.kind && Objects.equals(methods, that.methods) && Objects.equals(fields, that.fields) && Objects.equals(superClass, that.superClass) && Objects.equals(interfaces, that.interfaces) && Objects.equals(annotations, that.annotations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, modifiers, kind, methods, fields, superClass, interfaces, annotations);
    }
}
