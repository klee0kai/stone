package com.github.klee0kai.stone.helpers.invokecall;

import com.github.klee0kai.stone._hidden_.types.ListUtils;
import com.github.klee0kai.stone.model.FieldDetail;
import com.github.klee0kai.stone.model.Pair;
import com.github.klee0kai.stone.wrappers.PhantomProvide;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static com.github.klee0kai.stone.helpers.wrap.WrapHelper.*;

public class GenArgumentFunctions {

    /**
     * Simple unwrap argument from wrapped type (WeakReference and other).
     * If you have arguments like <br>
     * {@code Provide<SomeClass> someVariable;}  <br>
     * unwrapArgument support to unwrap SomeClass type on invoke method, like <br>
     * {@code doSmth(someVariable.provide()) }
     *
     * @param envFields available fields
     * @return unwrapped field get code, or null
     */
    public static Function<FieldDetail, CodeBlock> unwrapArgument(Collection<FieldDetail> envFields) {
        return arg -> {
            boolean isWannaList = isList(arg.type);
            List<FieldDetail> typeFields = ListUtils.filter(envFields, (i, f) ->
                    Objects.equals(nonWrappedType(f.type), nonWrappedType(arg.type)));
            FieldDetail field = isWannaList ? ListUtils.first(typeFields, (i, f) ->
                    isList(f.type) && Objects.equals(f.qualifierAnns, arg.qualifierAnns)) : null;

            if (field == null) {
                //non list
                field = ListUtils.first(typeFields, (i, f) -> Objects.equals(f.qualifierAnns, arg.qualifierAnns));
            }

            if (field != null) {
                return transform(field.type, arg.type, CodeBlock.of(field.name));
            }
            return null;
        };
    }

}
