package com.github.klee0kai.stone.helpers;

import com.github.klee0kai.stone._hidden_.IModule;
import com.github.klee0kai.stone._hidden_.IPrivateComponent;
import com.github.klee0kai.stone._hidden_.types.ListUtils;
import com.github.klee0kai.stone.annotations.component.GcScopeAnnotation;
import com.github.klee0kai.stone.exceptions.ClassNotFoundStoneException;
import com.github.klee0kai.stone.lifecycle.StoneLifeCycleOwner;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.annotations.ComponentAnn;
import com.github.klee0kai.stone.utils.ClassNameUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import javax.inject.Named;
import javax.inject.Qualifier;
import javax.inject.Scope;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.util.*;

import static com.github.klee0kai.stone.exceptions.ExceptionStringBuilder.createErrorMes;

/**
 * Index all elements in projects.
 */
public class AllClassesHelper {

    private Elements elements;
    private final Set<TypeName> lifeCycleOwners = new HashSet<>();
    private final Map<String, ClassDetail> gcScopeAnnotations = new HashMap<>();
    private final Map<String, ClassDetail> qualifierAnnotations = new HashMap<>();

    public final Set<ClassName> allIdentifiers = new HashSet<>();

    public ClassDetail iComponentClassDetails;
    public ClassDetail iModule;
    public ClassDetail iLifeCycleOwner;

    public TypeElement scopeAnnotationElement;
    public TypeElement gcScopeAnnotationElement;

    public TypeElement qualifierAnnotationElement;


    /**
     * Index common classes using in lib
     *
     * @param elements annotation processor's elements
     */
    public void init(Elements elements) {
        this.elements = elements;
        iComponentClassDetails = findForType(ClassName.get(IPrivateComponent.class));
        iModule = findForType(ClassName.get(IModule.class));
        iLifeCycleOwner = findForType(ClassName.get(StoneLifeCycleOwner.class));

        scopeAnnotationElement = typeElementFor(ClassName.get(Scope.class));
        gcScopeAnnotationElement = typeElementFor(ClassName.get(GcScopeAnnotation.class));
        qualifierAnnotationElement = typeElementFor(ClassName.get(Qualifier.class));

        TypeName namedType = ClassName.get(Named.class);
        qualifierAnnotations.putIfAbsent(namedType.toString(), findForType(namedType));
    }

    /**
     * Collect all using GcScope annotations in component and all in component's parents.
     * After the method you can use findGcScopeAnnotation to get annotation details
     *
     * @param classDetail component's ClassDetail object
     */
    public void deepExtractAdditionalClasses(ClassDetail classDetail) {
        for (ClassDetail parent : classDetail.getAllParents(false)) {
            TypeElement parentEl = typeElementFor(parent.className);
            ComponentAnn parentCompAnn = parent.ann(ComponentAnn.class);
            if (parentCompAnn != null) allIdentifiers.addAll(parentCompAnn.identifiers);

            for (Element methodEl : parentEl.getEnclosedElements()) {
                for (AnnotationMirror ann : methodEl.getAnnotationMirrors()) {
                    List<? extends AnnotationMirror> methodAnnotations = ann.getAnnotationType().asElement().getAnnotationMirrors();
                    // add as GC Scope
                    boolean isScopeAnnotated = ListUtils.contains(methodAnnotations, (inx, it) -> {
                        Element annEl = it.getAnnotationType().asElement();
                        return Objects.equals(annEl, scopeAnnotationElement) || Objects.equals(annEl, gcScopeAnnotationElement);
                    });
                    if (isScopeAnnotated) {
                        String annClName = ann.getAnnotationType().toString();
                        ClassDetail annClDetails = new ClassDetail(typeElementFor(annClName));
                        gcScopeAnnotations.put(annClDetails.className.toString(), annClDetails);
                    }

                    // add as Qualifier Annotation
                    boolean isQualifierAnnotated = ListUtils.contains(methodAnnotations, (inx, it) -> {
                        Element annEl = it.getAnnotationType().asElement();
                        return Objects.equals(annEl, qualifierAnnotationElement);
                    });
                    if (isQualifierAnnotated) {
                        String annClName = ann.getAnnotationType().toString();
                        ClassDetail annClDetails = new ClassDetail(typeElementFor(annClName));
                        qualifierAnnotations.put(annClDetails.className.toString(), annClDetails);
                    }
                }
            }
        }
    }


    /**
     * Get annotation details for annotation name
     *
     * @param annTypeName full class name
     * @return ClassDetail object of the annotation if found
     */
    public ClassDetail findGcScopeAnnotation(String annTypeName) {
        return gcScopeAnnotations.getOrDefault(annTypeName, null);
    }

    public ClassDetail findQualifierAnnotation(String annTypeName) {
        return qualifierAnnotations.getOrDefault(annTypeName, null);
    }

    /**
     * Check that class is implement stone lifecycle owner.
     * {@link StoneLifeCycleOwner}
     *
     * @param typeName class name
     * @return true if this class implement  {@link StoneLifeCycleOwner}
     */
    public boolean isLifeCycleOwner(TypeName typeName) {
        if (lifeCycleOwners.contains(typeName))
            return true;
        ClassDetail cl = findForType(typeName);
        if (cl.isExtOf(iLifeCycleOwner.className)) {
            lifeCycleOwners.add(typeName);
            return true;
        }
        return false;
    }

    /**
     * Find class details of type.
     * May throw exception
     *
     * @param typeName type name
     * @return {@link ClassDetail} if found
     */
    public ClassDetail findForType(TypeName typeName) {
        try {
            TypeName rawType = ClassNameUtils.rawTypeOf(typeName);
            if (rawType instanceof ClassName) {
                ClassDetail cl = new ClassDetail(elements.getTypeElement(((ClassName) rawType).canonicalName()));
                cl.className = typeName;
                return cl;
            }
            return null;
        } catch (Exception e) {
            throw new ClassNotFoundStoneException(
                    createErrorMes()
                            .classNonFound(typeName.toString())
                            .build(),
                    e
            );
        }
    }

    /**
     * Find class details of type.
     *
     * @param typeName type name
     * @return {@link ClassDetail} if found or null
     */
    public ClassDetail tryFindForType(TypeName typeName) {
        try {
            return findForType(typeName);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Find compile element  of typename
     *
     * @param typeName typename
     * @return compile element if found
     */
    public TypeElement typeElementFor(TypeName typeName) {
        if (typeName instanceof ClassName)
            return elements.getTypeElement(((ClassName) typeName).canonicalName());
        return null;
    }

    /**
     * Find compile element  of typename
     *
     * @param canonicalName type canonical name
     * @return compile element if found
     */
    public TypeElement typeElementFor(String canonicalName) {
        return elements.getTypeElement(canonicalName);
    }

    public TypeElement typeElementFor(ClassName canonicalName) {
        return elements.getTypeElement(canonicalName.canonicalName());
    }

}
