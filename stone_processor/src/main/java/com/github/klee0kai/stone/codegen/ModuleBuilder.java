package com.github.klee0kai.stone.codegen;

import com.github.klee0kai.stone.interfaces.IModule;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.utils.ClassNameUtils;
import com.github.klee0kai.stone.utils.CodeFileUtil;
import com.squareup.javapoet.*;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ModuleBuilder {

    public final ClassDetail orModuleCl;

    public ClassName className;

    public static String factoryFieldName = "factory";

    private static String appliedLocalFieldName = "applied";

    public static String initMethodName = "init";
    public static String extOfMethodName = "extOf";
    public static String getFactoryMethodName = "getFactory";

    public static ClassName softRefClassName = ClassName.get(SoftReference.class);
    public static ClassName weakRefClassName = ClassName.get(WeakReference.class);

    public final Set<TypeName> interfaces = new HashSet<>();

    // ---------------------- common fields and method  ----------------------------------
    public final HashMap<String, FieldSpec.Builder> fields = new HashMap<>();

    public final HashMap<String, MethodSpec.Builder> iModuleMethodBuilders = new HashMap<>();


    // ---------------------- provide fields and method  ----------------------------------
    public final HashMap<String, FieldSpec.Builder> cacheFields = new HashMap<>();
    public final List<MethodSpec.Builder> provideMethodBuilders = new LinkedList<>();

    private boolean collected = false;

    public static ModuleBuilder from(ModuleFactoryBuilder factoryBuilder) {
        ModuleBuilder builder = new ModuleBuilder(factoryBuilder.orFactory)
                .factoryField(factoryBuilder.orFactory.className, factoryBuilder.className)
                .implementIModule();

        for (MethodDetail m : factoryBuilder.orFactory.getAllMethods(false)) {
            if (Objects.equals(m.methodName, "<init>"))
                continue;
            if (m.bindInstanceAnnotation != null)
                switch (m.bindInstanceAnnotation.cacheType) {
                    case WEAK:
                        builder.bindInstanceRef(m.methodName, m.returnType, weakRefClassName);
                        break;
                    case SOFT:
                        builder.bindInstanceRef(m.methodName, m.returnType, softRefClassName);
                        break;
                    case STRONG:
                        builder.bindInstanceStrong(m.methodName, m.returnType);
                        break;
                }
            else if (m.provideAnnotation != null) {
                switch (m.provideAnnotation.cacheType) {
                    case FACTORY:
                        builder.provideFactory(m.methodName, m.returnType);
                        break;
                    case WEAK:
                        builder.provideRefCached(m.methodName, m.returnType, weakRefClassName);
                        break;
                    case SOFT:
                        builder.provideRefCached(m.methodName, m.returnType, softRefClassName);
                        break;
                    case STRONG:
                        builder.provideStrongCached(m.methodName, m.returnType);
                        break;
                }
            } else
                builder.provideRefCached(m.methodName, m.returnType, softRefClassName);
        }
        return builder;
    }


    public ModuleBuilder(ClassDetail orModuleCl) {
        this.orModuleCl = orModuleCl;
        this.className = ClassNameUtils.genModuleNameMirror(orModuleCl.className);
    }

    public ModuleBuilder factoryField(TypeName typeName, TypeName initTypeName) {
        FieldSpec.Builder builder = FieldSpec.builder(typeName, factoryFieldName, Modifier.PRIVATE);
        if (initTypeName != null)
            builder.initializer("new $T()", initTypeName);
        fields.put(factoryFieldName, builder);
        return this;
    }


    /**
     * Call first
     * add IModule methods.
     *
     * @return
     */
    public ModuleBuilder implementIModule() {
        boolean hasSupperStoneModule = orModuleCl.superClass != null && orModuleCl.superClass.moduleAnn != null;
        interfaces.add(ClassName.get(IModule.class));
        initMethod(true);
        extOfMethod(hasSupperStoneModule ? ClassNameUtils.genModuleNameMirror(orModuleCl.superClass.className) : null, true);
        getFactoryMethod(true);
        return this;
    }

    public ModuleBuilder initMethod(boolean override) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(initMethodName)
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                .addParameter(Object.class, "or")
                .addStatement("boolean $L = false", appliedLocalFieldName)
                .beginControlFlow("if (or instanceof $T) ", orModuleCl.className)
                .addStatement("this.$L = ($T) or", factoryFieldName, orModuleCl.className)
                .addStatement("$L = true", appliedLocalFieldName)
                .endControlFlow()
                // get module factory by module class
                .beginControlFlow("if (or instanceof Class<?> && ((Class<?>) or).isInstance($T.class))", orModuleCl.className)
                .beginControlFlow("try")
                .addComment("looking for generated factory for module class")
                .addStatement("Class<?> gennedClass = Class.forName(((Class) or).getCanonicalName() + \"StoneFactory\")")
                .addStatement("$L = ($T) gennedClass.getConstructors()[0].newInstance()", factoryFieldName, orModuleCl.className)
                .addStatement("$L = true", appliedLocalFieldName)
                .endControlFlow()
                .beginControlFlow("catch ( $T | $T | $T | $T e)", ClassNotFoundException.class,
                        InstantiationException.class, IllegalAccessException.class, InvocationTargetException.class)

                .beginControlFlow("try")
                .addComment("we don't got stone factory. We suppose class have constructor")
                .addStatement("$L = ($T) ((Class<?>) or).getConstructors()[0].newInstance()", factoryFieldName, orModuleCl.className)
                .addStatement("$L = true", appliedLocalFieldName)
                .endControlFlow()
                .beginControlFlow("catch ($T | $T | $T ex)",
                        InstantiationException.class, IllegalAccessException.class, InvocationTargetException.class)
                .endControlFlow()

                .endControlFlow()
                .endControlFlow()
                .returns(boolean.class);

        if (override) builder.addAnnotation(Override.class);

        iModuleMethodBuilders.put(initMethodName, builder);
        return this;
    }


    /**
     * If superClass is module. We should implement extOf method.
     *
     * @param superStoneModule
     * @return
     */
    public ModuleBuilder extOfMethod(TypeName superStoneModule, boolean override) {
        MethodSpec.Builder extOfMethodBuilder = MethodSpec.methodBuilder(extOfMethodName)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(IModule.class, "superStoneModule")
                .returns(void.class);

        if (override) extOfMethodBuilder.addAnnotation(Override.class);

        if (superStoneModule != null) {
            String superDIModuleStoneFieldName = "superStone";
            fields.put(superDIModuleStoneFieldName,
                    FieldSpec.builder(superStoneModule, superDIModuleStoneFieldName, Modifier.PRIVATE));

            extOfMethodBuilder.beginControlFlow("if (superStoneModule instanceof $T)", superStoneModule)
                    .addStatement("this.$L = ($T) superStoneModule", superDIModuleStoneFieldName, superStoneModule)
                    .endControlFlow()
                    .beginControlFlow("else if (superStoneModule == null)")
                    .addStatement("this.$L = null ", superDIModuleStoneFieldName)
                    .endControlFlow();
        }
        iModuleMethodBuilders.put(extOfMethodName, extOfMethodBuilder);
        return this;
    }

    public ModuleBuilder getFactoryMethod(boolean override) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(getFactoryMethodName)
                .addModifiers(Modifier.PUBLIC)
                .returns(Object.class)
                .addStatement("return this.$L", factoryFieldName);
        if (override) builder.addAnnotation(Override.class);
        iModuleMethodBuilders.put(getFactoryMethodName, builder);
        return this;
    }


    public ModuleBuilder bindInstanceStrong(String name, TypeName typeName) {
        String cacheFieldName = name + "Strong";
        MethodSpec.Builder initMethodBuilder = iModuleMethodBuilders.get(initMethodName);
        cacheFields.put(cacheFieldName, FieldSpec.builder(typeName, cacheFieldName, Modifier.PRIVATE).initializer("null"));

        provideMethodBuilders.add(MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                .returns(typeName)
                .addStatement("return this.$L", cacheFieldName));

        if (initMethodBuilder != null)
            initMethodBuilder.beginControlFlow("if (or instanceof $T) ", typeName)
                    .addStatement("this.$L = ($T) or", cacheFieldName, typeName)
                    .addStatement("$L = true", appliedLocalFieldName)
                    .endControlFlow();

        return this;
    }

    public ModuleBuilder bindInstanceRef(String name, TypeName typeName, ClassName javaRef) {
        String cacheFieldName = name + "Ref";
        ParameterizedTypeName cacheType = ParameterizedTypeName.get(javaRef, typeName);
        MethodSpec.Builder initMethodBuilder = iModuleMethodBuilders.get(initMethodName);

        cacheFields.put(cacheFieldName, FieldSpec.builder(cacheType, cacheFieldName, Modifier.PRIVATE).initializer("null"));

        provideMethodBuilders.add(MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                .returns(typeName)
                .addStatement("return this.$L.get()", cacheFieldName));

        if (initMethodBuilder != null)
            initMethodBuilder.beginControlFlow("if (or instanceof $T) ", typeName)
                    .addStatement("this.$L = new $T(($T) or)", cacheFieldName, cacheType, typeName)
                    .addStatement("$L = true", appliedLocalFieldName)
                    .endControlFlow();
        return this;
    }

    public ModuleBuilder provideFactory(String name, TypeName typeName) {
        provideMethodBuilders.add(MethodSpec.methodBuilder(name)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                .returns(typeName)
                .addStatement("return  $L.$L()", factoryFieldName, name));
        return this;
    }


    public ModuleBuilder provideStrongCached(String name, TypeName typeName) {
        String getCachedMethodName = getCachedMethodName(name);
        String cacheFieldName = name + "Strong";
        cacheFields.put(cacheFieldName, FieldSpec.builder(typeName, cacheFieldName, Modifier.PRIVATE).initializer("null"));
        provideMethodBuilders.add(MethodSpec.methodBuilder(name)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                .returns(typeName)
                .addStatement("if ($L != null) return $L", cacheFieldName, cacheFieldName)
                .addStatement("return  this.$L = $L.$L()", cacheFieldName, factoryFieldName, name));
        provideMethodBuilders.add(MethodSpec.methodBuilder(getCachedMethodName)
                .addModifiers(Modifier.PROTECTED, Modifier.SYNCHRONIZED)
                .returns(typeName)
                .addStatement("return this.$L", cacheFieldName));
        return this;
    }

    public ModuleBuilder provideRefCached(String name, TypeName typeName, ClassName javaRef) {
        String getCachedMethodName = getCachedMethodName(name);
        String cacheFieldName = name + "Ref";
        ParameterizedTypeName cacheType = ParameterizedTypeName.get(javaRef, typeName);
        cacheFields.put(cacheFieldName, FieldSpec.builder(cacheType, cacheFieldName, Modifier.PRIVATE).initializer("null"));
        provideMethodBuilders.add(MethodSpec.methodBuilder(name)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                .returns(typeName)
                .addStatement("if ($L != null && $L.get() != null) return $L.get()", cacheFieldName, cacheFieldName, cacheFieldName)
                .addStatement("this.$L = new $T($L.$L())", cacheFieldName, cacheType, factoryFieldName, name)
                .addStatement("return this.$L.get()", cacheFieldName));
        provideMethodBuilders.add(MethodSpec.methodBuilder(getCachedMethodName)
                .addModifiers(Modifier.PROTECTED, Modifier.SYNCHRONIZED)
                .returns(typeName)
                .addStatement("return this.$L.get()", cacheFieldName));
        return this;
    }

    public ModuleBuilder collect() {
        if (collected)
            return this;
        collected = true;
        MethodSpec.Builder initMethod = iModuleMethodBuilders.get(initMethodName);
        if (initMethod != null)
            initMethod.addStatement("return false");
        return this;
    }

    public TypeSpec build() {
        collect();

        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(className);
        typeSpecBuilder.addModifiers(Modifier.PUBLIC);
        if (orModuleCl.isInterfaceClass())
            typeSpecBuilder.addSuperinterface(orModuleCl.className);
        else typeSpecBuilder.superclass(orModuleCl.className);

        for (TypeName supInterface : interfaces)
            typeSpecBuilder.addSuperinterface(supInterface);

        for (FieldSpec.Builder fieldBuilder : fields.values())
            typeSpecBuilder.addField(fieldBuilder.build());
        for (FieldSpec.Builder cacheFieldBuilder : cacheFields.values())
            typeSpecBuilder.addField(cacheFieldBuilder.build());

        for (MethodSpec.Builder iModuleMethod : iModuleMethodBuilders.values())
            typeSpecBuilder.addMethod(iModuleMethod.build());

        for (MethodSpec.Builder provideMethod : provideMethodBuilders)
            typeSpecBuilder.addMethod(provideMethod.build());

        return typeSpecBuilder.build();
    }

    public void writeTo(Filer filer) {
        TypeSpec typeSpec = build();
        if (typeSpec != null)
            CodeFileUtil.writeToJavaFile(className.packageName(), typeSpec);
    }


    private static String getCachedMethodName(String factoryMethodName) {
        return "__" + factoryMethodName + "_cache";
    }
}
