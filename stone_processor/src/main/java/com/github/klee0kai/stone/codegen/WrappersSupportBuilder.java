package com.github.klee0kai.stone.codegen;

import com.github.klee0kai.stone.codegen.model.WrapperCreatorField;
import com.github.klee0kai.stone.helpers.codebuilder.SmartCode;
import com.github.klee0kai.stone.helpers.wrap.WrapHelper;
import com.github.klee0kai.stone.helpers.wrap.WrapType;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.annotations.WrapperCreatorsAnn;
import com.github.klee0kai.stone.utils.CodeFileUtil;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.LinkedList;

import static com.github.klee0kai.stone.helpers.wrap.WrapHelper.paramType;

public class WrappersSupportBuilder {

    public ClassName className;

    public static final String provideWrappersGlFieldPrefixName = "__wrapperCreator";
    public final LinkedList<WrapperCreatorField> wrapperCreatorFields = new LinkedList<>();


    public WrappersSupportBuilder(ClassName className) {
        this.className = className;
    }

    public WrappersSupportBuilder addProvideWrapperField(ClassDetail provideWrappersCl) {
        String name = provideWrappersGlFieldPrefixName + wrapperCreatorFields.size();
        wrapperCreatorFields.add(new WrapperCreatorField(
                name,
                provideWrappersCl.ann(WrapperCreatorsAnn.class).wrappers,
                FieldSpec.builder(provideWrappersCl.className, name, Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                        .initializer("new $T()", provideWrappersCl.className)
        ));

        for (ClassName wrapper : provideWrappersCl.ann(WrapperCreatorsAnn.class).wrappers) {
            WrapType wrapType = new WrapType();
            //TODO https://github.com/klee0kai/stone/issues/68
            wrapType.isNoCachingWrapper = false;
            wrapType.typeName = wrapper;
            wrapType.wrap = (or -> {
                SmartCode builder = SmartCode.builder()
                        .add(CodeBlock.of("$T.$L.wrap( $T.class , () -> ", className, name, wrapper))
                        .add(or)
                        .add(")");
                if (or.providingType != null)
                    builder.providingType(ParameterizedTypeName.get(wrapper, or.providingType));
                return builder;
            });
            wrapType.unwrap = (or) -> {
                SmartCode builder = SmartCode.builder()
                        .add(CodeBlock.of("$T.$L.unwrap( ", className, name))
                        .add(or)
                        .add(")");
                if (or.providingType != null)
                    builder.providingType(paramType(or.providingType));
                return builder;
            };

            WrapHelper.support(wrapType);
        }
        return this;
    }

    public boolean isEmpty() {
        return wrapperCreatorFields.isEmpty();
    }

    public TypeSpec build() {
        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        for (WrapperCreatorField field : wrapperCreatorFields)
            typeSpecBuilder.addField(field.fieldBuilder.build());

        return typeSpecBuilder.build();
    }


    public TypeSpec buildAndWrite() {
        TypeSpec typeSpec = build();
        if (typeSpec != null) {
            CodeFileUtil.writeToJavaFile(className.packageName(), typeSpec);
        }

        return typeSpec;
    }

}
