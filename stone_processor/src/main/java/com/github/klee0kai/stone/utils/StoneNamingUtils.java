package com.github.klee0kai.stone.utils;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

/**
 * Stone's class naming utils
 */
public class StoneNamingUtils {

    public static String COMPONENT_NAME_SUFFIX = "StoneComponent";
    public static String FACTORY_NAME_SUFFIX = "_FStone";
    public static String MODULE_NAME_SUFFIX = "_MStone";
    public static String CACHE_CONTROL_NAME_SUFFIX = "_CCMStone";
    public static String HIDDEN_MODULE_NAME_SUFFIX = "_HMStone";
    public static String TYPE_WRAPPER_NAME_SUFFIX = "_TWStone";

    /**
     * Component's class name creator
     *
     * @param or original user's component's class type
     * @return stone component class name
     */
    public static ClassName genComponentNameMirror(TypeName or) {
        if (!(or instanceof ClassName)) return null;
        ClassName origin = (ClassName) or;
        return ClassName.get(origin.packageName(), origin.simpleName() + COMPONENT_NAME_SUFFIX);
    }

    /**
     * Factory's class name creator
     *
     * @param or original user's module's class type
     * @return stone factory class name
     */
    public static ClassName genFactoryNameMirror(TypeName or) {
        if (!(or instanceof ClassName)) return null;
        ClassName origin = (ClassName) or;
        return ClassName.get(origin.packageName(), origin.simpleName() + FACTORY_NAME_SUFFIX);
    }


    /**
     * Module's class name creator
     *
     * @param or original user's module's class type
     * @return stone module class name
     */
    public static ClassName genModuleNameMirror(TypeName or) {
        if (!(or instanceof ClassName)) return null;
        ClassName origin = (ClassName) or;
        return ClassName.get(origin.packageName(), origin.simpleName() + MODULE_NAME_SUFFIX);
    }


    /**
     * CacheControl's class name creator
     *
     * @param or original user's module's class type
     * @return stone cache-control class name
     */
    public static ClassName genCacheControlInterfaceModuleNameMirror(TypeName or) {
        if (!(or instanceof ClassName)) return null;
        ClassName origin = (ClassName) or;
        return ClassName.get(origin.packageName(), origin.simpleName() + CACHE_CONTROL_NAME_SUFFIX);
    }

    /**
     * HiddenModule's class name creator
     *
     * @param or original user's module's class type
     * @return stone hidden-module class name
     */
    public static ClassName genHiddenModuleNameMirror(TypeName or) {
        if (!(or instanceof ClassName)) return null;
        ClassName origin = (ClassName) or;
        return ClassName.get(origin.packageName(), origin.simpleName() + HIDDEN_MODULE_NAME_SUFFIX);
    }


    public static ClassName typeWrappersClass(TypeName or) {
        if (!(or instanceof ClassName)) return null;
        ClassName origin = (ClassName) or;
        return ClassName.get(origin.packageName(), origin.simpleName() + TYPE_WRAPPER_NAME_SUFFIX);
    }

}
