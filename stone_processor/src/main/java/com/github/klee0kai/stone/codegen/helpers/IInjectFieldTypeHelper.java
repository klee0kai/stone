package com.github.klee0kai.stone.codegen.helpers;

import com.github.klee0kai.stone.types.LazyProvide;
import com.github.klee0kai.stone.types.PhantomProvide;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Objects;

public interface IInjectFieldTypeHelper {

    TypeName providingType();

    CodeBlock codeInjectField(String objName, String fieldName, CodeBlock provideCode);


    static IInjectFieldTypeHelper findHelper(TypeName fieldType) {

        if (fieldType instanceof ParameterizedTypeName) {
            ParameterizedTypeName typeName = (ParameterizedTypeName) fieldType;

            if (!typeName.typeArguments.isEmpty() && Objects.equals(ClassName.get(WeakReference.class), typeName.rawType)
                    || Objects.equals(ClassName.get(SoftReference.class), typeName.rawType)) {
                return new RefFieldHelper(typeName.rawType, typeName.typeArguments.get(0));
            }
            if (!typeName.typeArguments.isEmpty() && Objects.equals(ClassName.get(PhantomProvide.class), typeName.rawType)
                    || Objects.equals(ClassName.get(LazyProvide.class), typeName.rawType)) {
                return new ProvideFieldHelper(typeName.rawType, typeName.typeArguments.get(0));
            }
        }
        return new SimpleFieldHelper(fieldType);
    }

    class SimpleFieldHelper implements IInjectFieldTypeHelper {
        private final TypeName typeName;

        public SimpleFieldHelper(TypeName typeName) {
            this.typeName = typeName;
        }

        @Override
        public TypeName providingType() {
            return typeName;
        }

        @Override
        public CodeBlock codeInjectField(String objName, String fieldName, CodeBlock provideCode) {
            return CodeBlock.builder().add("$L.$L = ", objName, fieldName)
                    .add(provideCode)
                    .build();
        }
    }


    class RefFieldHelper implements IInjectFieldTypeHelper {

        private final ClassName refType;
        private final TypeName typeName;

        public RefFieldHelper(ClassName refType, TypeName typeName) {
            this.refType = refType;
            this.typeName = typeName;
        }

        @Override
        public TypeName providingType() {
            return typeName;
        }

        @Override
        public CodeBlock codeInjectField(String objName, String fieldName, CodeBlock provideCode) {
            ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(refType, typeName);
            return CodeBlock.builder().add("$L.$L = new $T(", objName, fieldName, parameterizedTypeName)
                    .add(provideCode)
                    .add(")")
                    .build();
        }
    }


    class ProvideFieldHelper implements IInjectFieldTypeHelper {

        private final ClassName refType;
        private final TypeName typeName;

        public ProvideFieldHelper(ClassName refType, TypeName typeName) {
            this.refType = refType;
            this.typeName = typeName;
        }

        @Override
        public TypeName providingType() {
            return typeName;
        }

        @Override
        public CodeBlock codeInjectField(String objName, String fieldName, CodeBlock provideCode) {
            ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(refType, typeName);
            return CodeBlock.builder().add("$L.$L = new $T( () -> { return ", objName, fieldName, parameterizedTypeName)
                    .add(provideCode)
                    .add("; })")
                    .build();
        }
    }

}
