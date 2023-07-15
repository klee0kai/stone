package com.github.klee0kai.stone.helpers.invokecall;

import com.github.klee0kai.stone.model.annotations.QualifierAnn;
import com.squareup.javapoet.TypeName;

import java.util.Objects;
import java.util.Set;

import static com.github.klee0kai.stone.helpers.wrap.WrapHelper.listWrapTypeIfNeed;

public class ProvideDep {

    public String methodName = null;

    public TypeName typeName;
    public Set<QualifierAnn> qualifierAnns;

    public ProvideDep(TypeName typeName, Set<QualifierAnn> qualifierAnns) {
        this.typeName = listWrapTypeIfNeed(typeName);
        this.qualifierAnns = qualifierAnns;
    }

    public ProvideDep(String methodName, TypeName typeName, Set<QualifierAnn> qualifierAnns) {
        this.methodName = methodName;
        this.typeName = typeName;
        this.qualifierAnns = qualifierAnns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProvideDep that = (ProvideDep) o;
        return Objects.equals(typeName, that.typeName) && Objects.equals(qualifierAnns, that.qualifierAnns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeName, qualifierAnns);
    }
}
