package com.github.klee0kai.stone.helpers;

import com.github.klee0kai.stone.codegen.model.WrapperCreatorField;
import com.github.klee0kai.stone.types.wrappers.Ref;
import com.github.klee0kai.stone.types.wrappers.LazyProvide;
import com.github.klee0kai.stone.types.wrappers.PhantomProvide;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import javax.inject.Provider;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;

public interface IProvideTypeWrapperHelper {


    /**
     * original value type
     *
     * @return
     */
    TypeName providingType();

    /**
     * inject value code
     *
     * @param provideCode original value provide code
     * @return
     */
    CodeBlock provideCode(CodeBlock provideCode);


    /**
     * every time call DI provide methods.
     * Nothing to protect
     *
     * @return
     */
    boolean isGenerateWrapper();

    static IProvideTypeWrapperHelper findHelper(TypeName fieldType, List<WrapperCreatorField> creators) {
        if (fieldType instanceof ParameterizedTypeName) {
            ParameterizedTypeName typeName = (ParameterizedTypeName) fieldType;
            if (!typeName.typeArguments.isEmpty()) {
                if (Objects.equals(ClassName.get(WeakReference.class), typeName.rawType)
                        || Objects.equals(ClassName.get(SoftReference.class), typeName.rawType)) {
                    return new RefFieldWrapperHelper(
                            typeName.rawType,
                            typeName.typeArguments.get(0)
                    );
                }

                ClassName phantomProvide = ClassName.get(PhantomProvide.class);
                if (Objects.equals(phantomProvide, typeName.rawType)
                        || Objects.equals(ClassName.get(Ref.class), typeName.rawType)
                        || Objects.equals(ClassName.get(Provider.class), typeName.rawType))
                    return new ProvideFieldWrapperHelper(
                            phantomProvide,
                            typeName.typeArguments.get(0),
                            true
                    );

                if (Objects.equals(ClassName.get(LazyProvide.class), typeName.rawType)) {
                    return new ProvideFieldWrapperHelper(
                            typeName.rawType,
                            typeName.typeArguments.get(0),
                            false
                    );
                }

                for (WrapperCreatorField creator : creators) {
                    if (creator.isSupport(typeName.rawType)) {
                        return new ProvideExternalWrapperHelper(
                                typeName.rawType,
                                typeName.typeArguments.get(0),
                                creator
                        );
                    }
                }
            }
        }
        return new SimpleFieldWrapperHelper(fieldType);
    }

    class SimpleFieldWrapperHelper implements IProvideTypeWrapperHelper {
        private final TypeName typeName;

        public SimpleFieldWrapperHelper(TypeName typeName) {
            this.typeName = typeName;
        }

        @Override
        public TypeName providingType() {
            return typeName;
        }

        @Override
        public CodeBlock provideCode(CodeBlock provideCode) {
            return provideCode;
        }

        @Override
        public boolean isGenerateWrapper() {
            return false;
        }
    }


    class RefFieldWrapperHelper implements IProvideTypeWrapperHelper {

        private final ClassName refType;
        private final TypeName typeName;

        public RefFieldWrapperHelper(ClassName refType, TypeName typeName) {
            this.refType = refType;
            this.typeName = typeName;
        }

        @Override
        public TypeName providingType() {
            return typeName;
        }

        @Override
        public CodeBlock provideCode(CodeBlock provideCode) {
            ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(refType, typeName);
            return CodeBlock.of(
                    "new $T( $L )",
                    parameterizedTypeName, provideCode
            );
        }

        @Override
        public boolean isGenerateWrapper() {
            return false;
        }
    }


    class ProvideFieldWrapperHelper implements IProvideTypeWrapperHelper {

        private final ClassName refType;
        private final TypeName typeName;
        private final boolean isGenerateWrapper;

        public ProvideFieldWrapperHelper(ClassName refType, TypeName typeName, boolean isGenerateWrapper) {
            this.refType = refType;
            this.typeName = typeName;
            this.isGenerateWrapper = isGenerateWrapper;
        }

        @Override
        public TypeName providingType() {
            return typeName;
        }

        @Override
        public CodeBlock provideCode(CodeBlock provideCode) {
            ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(refType, typeName);
            return CodeBlock.of(
                    "new $T( () -> { return $L ; } )",
                    parameterizedTypeName, provideCode
            );
        }

        @Override
        public boolean isGenerateWrapper() {
            return isGenerateWrapper;
        }
    }

    class ProvideExternalWrapperHelper implements IProvideTypeWrapperHelper {

        private final ClassName refType;
        private final TypeName typeName;

        private final WrapperCreatorField creatorField;

        public ProvideExternalWrapperHelper(ClassName refType, TypeName typeName, WrapperCreatorField creatorField) {
            this.refType = refType;
            this.typeName = typeName;
            this.creatorField = creatorField;
        }

        @Override
        public TypeName providingType() {
            return typeName;
        }

        @Override
        public CodeBlock provideCode(CodeBlock provideCode) {
            ParameterizedTypeName providerTypeName = ParameterizedTypeName.get(ClassName.get(PhantomProvide.class), typeName);
            return CodeBlock.of(
                    "$L.provideWrapped($T.class, new $T( () -> { return $L ; } ))",
                    creatorField.fieldName, refType,
                    providerTypeName, provideCode
            );
        }


        @Override
        public boolean isGenerateWrapper() {
            //TODO https://github.com/klee0kai/stone/issues/14
            return false;
        }
    }

}
