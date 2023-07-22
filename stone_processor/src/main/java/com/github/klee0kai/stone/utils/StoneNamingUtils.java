package com.github.klee0kai.stone.utils;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

/**
 * Stone's class naming utils
 */
public class StoneNamingUtils {

    /**
     * Component's class name creator
     *
     * @param or original user's component's class type
     * @return stone component class name
     */
    public static ClassName genComponentNameMirror(TypeName or) {
        if (!(or instanceof ClassName)) return null;
        ClassName origin = (ClassName) or;
        return ClassName.get(origin.packageName(), origin.simpleName() + "StoneComponent");
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
        return ClassName.get(origin.packageName(), origin.simpleName() + "_FStone");
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
        return ClassName.get(origin.packageName(), origin.simpleName() + "_MStone");
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
        return ClassName.get(origin.packageName(), "I" + origin.simpleName() + "_CCMStone");
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
        return ClassName.get(origin.packageName(), origin.simpleName() + "_HMStone");
    }


    public static ClassName typeWrappersClass(TypeName or) {
        if (!(or instanceof ClassName)) return null;
        ClassName origin = (ClassName) or;
        return ClassName.get(origin.packageName(), origin.simpleName() + "_TWStone");
    }

}
