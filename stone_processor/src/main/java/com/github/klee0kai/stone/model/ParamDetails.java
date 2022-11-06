package com.github.klee0kai.stone.model;

import com.squareup.javapoet.TypeName;

import javax.lang.model.element.VariableElement;
import java.util.Objects;

public class ParamDetails {

    public String name;

    public TypeName type;

    public static ParamDetails of(VariableElement p) {
        ParamDetails paramDetails = new ParamDetails();
        paramDetails.type = TypeName.get(p.asType());
        paramDetails.name = p.getSimpleName().toString();
        return paramDetails;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParamDetails that = (ParamDetails) o;
        return Objects.equals(name, that.name) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }
}
