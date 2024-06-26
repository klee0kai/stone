package com.github.klee0kai.stone.model;

import com.github.klee0kai.stone._hidden_.types.ListUtils;
import com.github.klee0kai.stone.annotations.component.*;
import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.stone.model.annotations.*;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.lang.model.element.*;
import java.lang.annotation.Annotation;
import java.util.*;

import static com.github.klee0kai.stone.AnnotationProcessor.allClassesHelper;
import static com.github.klee0kai.stone.helpers.SetFieldHelper.capitalized;

/**
 * Collected method details of compile element or method specs.
 * Collect the information you need in one place
 */
public class MethodDetail implements Cloneable {

    public String methodName;

    public TypeName returnType;

    public Set<Modifier> modifiers = Collections.emptySet();

    public ElementKind elementKind;

    public List<FieldDetail> args = new LinkedList<>();

    public Map<Class<? extends IAnnotation>, IAnnotation> annotations = new HashMap<>();
    public LinkedList<TypeName> gcScopeAnnotations = new LinkedList<>();
    public Set<QualifierAnn> qualifierAnns = new HashSet<>();

    public Object defValue = null;

    public Element sourceEl;

    /**
     * Take method details from compile element
     *
     * @param element original element
     * @return new MethodDetail of element
     */
    public static MethodDetail of(ExecutableElement element) {
        MethodDetail methodDetail = new MethodDetail();
        methodDetail.methodName = element.getSimpleName().toString();
        methodDetail.returnType = TypeName.get(element.getReturnType());
        methodDetail.modifiers = element.getModifiers();
        methodDetail.elementKind = element.getKind();
        methodDetail.defValue = element.getDefaultValue() != null ? element.getDefaultValue().getValue() : null;
        methodDetail.sourceEl = element;


        methodDetail.addAnnotation(InitAnn.of(element.getAnnotation(Init.class)));
        methodDetail.addAnnotation(ExtOfAnn.of(element.getAnnotation(ExtendOf.class)));
        methodDetail.addAnnotation(ProvideAnn.of(element.getAnnotation(Provide.class)));
        methodDetail.addAnnotation(BindInstanceAnn.of(element.getAnnotation(BindInstance.class)));
        methodDetail.addAnnotation(ProtectInjectedAnn.of(element.getAnnotation(ProtectInjected.class)));
        methodDetail.addAnnotation(RunGcAnn.of(element.getAnnotation(RunGc.class)));
        methodDetail.addAnnotation(SwitchCacheAnn.of(element.getAnnotation(SwitchCache.class)));
        methodDetail.addAnnotation(InjectAnn.of(element.getAnnotation(Inject.class)));
        methodDetail.addAnnotation(SingletonAnn.of(element.getAnnotation(Singleton.class)));
        methodDetail.addAnnotation(ModuleOriginFactoryAnn.of(element.getAnnotation(ModuleOriginFactory.class)));

        List<Class<? extends Annotation>> scClasses = Arrays.asList(GcAllScope.class, GcWeakScope.class, GcSoftScope.class, GcStrongScope.class);
        for (Class<? extends Annotation> sc : scClasses)
            if (element.getAnnotation(sc) != null)
                methodDetail.gcScopeAnnotations.add(ClassName.get(sc));

        for (AnnotationMirror ann : element.getAnnotationMirrors()) {
            String clName = ann.getAnnotationType().toString();
            ClassDetail gcAnn = allClassesHelper.findGcScopeAnnotation(clName);
            if (gcAnn != null) methodDetail.gcScopeAnnotations.add(gcAnn.className);

            ClassDetail qualifierAnn = allClassesHelper.findQualifierAnnotation(clName);
            if (qualifierAnn != null) methodDetail.qualifierAnns.add(QualifierAnn.of(ann));
        }
        for (VariableElement v : element.getParameters()) {
            // get<Field>$annotations not checked for method's arguments
            String getAnnotationsElName = "get" + capitalized(v.getSimpleName().toString()) + "$annotations";
            Element getAnnotationsEl = ListUtils.first(element.getParameters(), (i, it) ->
                    Objects.equals(it.getSimpleName().toString(), getAnnotationsElName)
            );
            methodDetail.args.add(FieldDetail.of(v, getAnnotationsEl));
        }

        return methodDetail;
    }

    /**
     * Create new method without args and returns
     *
     * @param methodName method name
     * @return new FieldDetail object
     */
    public static MethodDetail simpleName(String methodName) {
        MethodDetail methodDetail = new MethodDetail();
        methodDetail.methodName = methodName;
        return methodDetail;
    }

    /**
     * Create new get type method.
     *
     * @param name     method name. Supposed to be like "getSmth"
     * @param typeName return type of method
     * @return new FieldDetail object
     */
    public static MethodDetail simpleGetMethod(String name, TypeName typeName) {
        MethodDetail m = new MethodDetail();
        m.methodName = name;
        m.returnType = typeName;
        return m;
    }

    /**
     * Create new set type method.
     *
     * @param name     method name. Supposed to be like "setSmth"
     * @param typeName arg type of method
     * @return new FieldDetail object
     */
    public static MethodDetail simpleSetMethod(String name, TypeName typeName) {
        MethodDetail m = new MethodDetail();
        m.methodName = name;
        m.args.add(FieldDetail.simple(name, typeName));
        m.returnType = TypeName.VOID;
        return m;
    }

    /**
     * Create new class constructor method.
     *
     * @param args list of args
     * @return new methodDetail object
     */
    public static MethodDetail constructorMethod(List<FieldDetail> args) {
        MethodDetail m = new MethodDetail();
        m.methodName = "<init>";
        m.args = args;
        return m;
    }

    /**
     * @return true if method has abstract modifier
     */
    public boolean isAbstract() {
        return modifiers.contains(Modifier.ABSTRACT);
    }

    /**
     * Methods are same if they have same name and argument types
     *
     * @param methodDetail method to compare
     * @return true if methods are same
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

    /**
     * Add annotation to method
     *
     * @param annotation annotation details
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
    public final boolean hasOnlyAnnotations(boolean allowGsScopeAnnotations, boolean allowQualifierAnnotations, Class<? extends IAnnotation>... aClasses) {
        if (annotations.size() > aClasses.length) return false;
        List<Class<? extends IAnnotation>> availableClasses = Arrays.asList(aClasses);
        for (Class<? extends IAnnotation> annotation : annotations.keySet()) {
            if (!availableClasses.contains(annotation)) return false;
        }
        return (allowGsScopeAnnotations || gcScopeAnnotations.isEmpty())
                && (allowQualifierAnnotations || qualifierAnns.isEmpty());
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
        return Objects.equals(methodName, that.methodName) && Objects.equals(returnType, that.returnType)
                && Objects.equals(args, that.args);
    }

    @Override
    public int hashCode() {
        return Objects.hash(methodName, returnType, args);
    }
}
