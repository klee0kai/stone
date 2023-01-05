package com.github.klee0kai.stone.codegen.helpers;

import com.github.klee0kai.stone.types.IRef;
import com.github.klee0kai.stone.types.LazyProvide;
import com.github.klee0kai.stone.types.PhantomProvide;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import javax.inject.Provider;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Objects;

public interface IInjectFieldTypeHelper {


    /**
     * original value type
     *
     * @return
     */
    TypeName providingType();

    /**
     * inject value code
     *
     * @param objName
     * @param name            field name or setField value method name
     * @param provideCode
     * @param isOverSetMethod inject over set field method
     * @return
     */
    CodeBlock codeInjectField(String objName, String name, CodeBlock provideCode, boolean isOverSetMethod);


    /**
     * get original value code
     *
     * @param objName
     * @param name            field name or getField value method name
     * @param isOverGetMethod get field value over get Method
     * @return
     */
    CodeBlock codeGetField(String objName, String name, boolean isOverGetMethod);


    /**
     * every time call DI provide methods.
     * Nothing to protect
     *
     * @return
     */
    boolean isGenerateWrapper();

    static IInjectFieldTypeHelper findHelper(TypeName fieldType) {

        if (fieldType instanceof ParameterizedTypeName) {
            ParameterizedTypeName typeName = (ParameterizedTypeName) fieldType;
            if (!typeName.typeArguments.isEmpty()) {
                if (Objects.equals(ClassName.get(WeakReference.class), typeName.rawType)
                        || Objects.equals(ClassName.get(SoftReference.class), typeName.rawType)) {
                    return new RefFieldHelper(typeName.rawType, typeName.typeArguments.get(0));
                }

                ClassName phantomProvide = ClassName.get(PhantomProvide.class);
                if (Objects.equals(phantomProvide, typeName.rawType)
                        || Objects.equals(ClassName.get(IRef.class), typeName.rawType)
                        || Objects.equals(ClassName.get(Provider.class), typeName.rawType))
                    return new ProvideFieldHelper(phantomProvide, typeName.typeArguments.get(0), true);

                if (Objects.equals(ClassName.get(LazyProvide.class), typeName.rawType)) {
                    return new ProvideFieldHelper(typeName.rawType, typeName.typeArguments.get(0), false);
                }
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
        public CodeBlock codeInjectField(String objName, String name, CodeBlock provideCode, boolean isOverSetMethod) {
            if (isOverSetMethod) {
                return CodeBlock.builder().add("$L.$L( ", objName, name)
                        .add(provideCode)
                        .add(" )")
                        .build();
            } else {
                return CodeBlock.builder().add("$L.$L = ", objName, name)
                        .add(provideCode)
                        .build();
            }
        }

        @Override
        public CodeBlock codeGetField(String objName, String name, boolean isOverGetMethod) {
            if (isOverGetMethod) {
                return CodeBlock.builder()
                        .add("$L.$L()", objName, name)
                        .build();
            } else {
                return CodeBlock.builder()
                        .add("$L.$L", objName, name)
                        .build();
            }
        }

        @Override
        public boolean isGenerateWrapper() {
            return false;
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
        public CodeBlock codeInjectField(String objName, String name, CodeBlock provideCode, boolean isOverSetMethod) {
            ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(refType, typeName);
            if (isOverSetMethod) {
                return CodeBlock.builder().add("$L.$L( new $T(", objName, name, parameterizedTypeName)
                        .add(provideCode)
                        .add("))")
                        .build();
            } else {
                return CodeBlock.builder().add("$L.$L = new $T(", objName, name, parameterizedTypeName)
                        .add(provideCode)
                        .add(")")
                        .build();
            }
        }

        @Override
        public CodeBlock codeGetField(String objName, String name, boolean isOverGetMethod) {
            if (isOverGetMethod) {
                return CodeBlock.builder()
                        .add("$L.$L().get()", objName, name)
                        .build();
            } else {
                return CodeBlock.builder()
                        .add("$L.$L.get()", objName, name)
                        .build();
            }
        }

        @Override
        public boolean isGenerateWrapper() {
            return false;
        }
    }


    class ProvideFieldHelper implements IInjectFieldTypeHelper {

        private final ClassName refType;
        private final TypeName typeName;
        private final boolean isGenerateWrapper;

        public ProvideFieldHelper(ClassName refType, TypeName typeName, boolean isGenerateWrapper) {
            this.refType = refType;
            this.typeName = typeName;
            this.isGenerateWrapper = isGenerateWrapper;
        }

        @Override
        public TypeName providingType() {
            return typeName;
        }

        @Override
        public CodeBlock codeInjectField(String objName, String name, CodeBlock provideCode, boolean isOverSetMethod) {
            ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(refType, typeName);
            if (isOverSetMethod) {
                return CodeBlock.builder().add("$L.$L( new $T( () -> { return ", objName, name, parameterizedTypeName)
                        .add(provideCode)
                        .add("; }))")
                        .build();
            } else {
                return CodeBlock.builder().add("$L.$L = new $T( () -> { return ", objName, name, parameterizedTypeName)
                        .add(provideCode)
                        .add("; })")
                        .build();
            }
        }

        @Override
        public CodeBlock codeGetField(String objName, String name, boolean isOverGetMethod) {
            if (isOverGetMethod) {
                return CodeBlock.builder()
                        .add("$L.$L().get()", objName, name)
                        .build();
            } else {
                return CodeBlock.builder()
                        .add("$L.$L.get()", objName, name)
                        .build();
            }
        }

        @Override
        public boolean isGenerateWrapper() {
            return isGenerateWrapper;
        }
    }

}
