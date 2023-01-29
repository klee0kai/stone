package com.github.klee0kai.stone.codegen.helpers;

import com.github.klee0kai.stone.annotations.component.GcScopeAnnotation;
import com.github.klee0kai.stone.closed.IModule;
import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.stone.interfaces.IComponent;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.types.lifecycle.IStoneLifeCycleOwner;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import javax.inject.Scope;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.util.*;

public class AllClassesHelper {

    private Elements elements;
    private final Set<TypeName> lifeCycleOwners = new HashSet<>();
    private final Map<String, ClassDetail> gcScopeAnnotations = new HashMap<>();

    public ClassDetail iComponentClassDetails;
    public ClassDetail iModule;
    public ClassDetail iLifeCycleOwner;

    public TypeElement scopeAnnotationElement;
    public TypeElement gcScopeAnnotationElement;

    public AllClassesHelper() {
    }

    public void init(Elements elements) {
        this.elements = elements;
        iComponentClassDetails = findForType(ClassName.get(IComponent.class));
        iModule = findForType(ClassName.get(IModule.class));
        iLifeCycleOwner = findForType(ClassName.get(IStoneLifeCycleOwner.class));

        scopeAnnotationElement = typeElementFor(ClassName.get(Scope.class));
        gcScopeAnnotationElement = typeElementFor(ClassName.get(GcScopeAnnotation.class));
    }

    public void deepExtractGcAnnotations(ClassDetail classDetail) {
        for (ClassDetail parent : classDetail.getAllParents(false)) {
            TypeElement clElement = typeElementFor(parent.className);
            for (Element m : clElement.getEnclosedElements()) {
                for (AnnotationMirror ann : m.getAnnotationMirrors()) {
                    List<? extends AnnotationMirror> annAnnotations = ann.getAnnotationType().asElement().getAnnotationMirrors();
                    boolean isScopeAnnotated = ListUtils.contains(annAnnotations, (inx, it) -> {
                        Element el = it.getAnnotationType().asElement();
                        return Objects.equals(el, scopeAnnotationElement) || Objects.equals(el, gcScopeAnnotationElement);
                    });
                    if (isScopeAnnotated) {
                        String clName = ann.getAnnotationType().toString();
                        ClassDetail annClDetails = ClassDetail.of(typeElementFor(clName));
                        gcScopeAnnotations.put(annClDetails.className.toString(), annClDetails);
                    }
                }
            }
        }
    }


    public ClassDetail findGcScopeAnnotation(String annTypeName) {
        return gcScopeAnnotations.getOrDefault(annTypeName, null);
    }


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

    public ClassDetail findForType(TypeName typeName) {
        if (typeName instanceof ClassName)
            return ClassDetail.of(elements.getTypeElement(((ClassName) typeName).canonicalName()));
        return null;
    }

    public TypeElement typeElementFor(TypeName typeName) {
        if (typeName instanceof ClassName)
            return elements.getTypeElement(((ClassName) typeName).canonicalName());
        return null;
    }

    public TypeElement typeElementFor(String canonicalName) {
        return elements.getTypeElement(canonicalName);
    }

}
