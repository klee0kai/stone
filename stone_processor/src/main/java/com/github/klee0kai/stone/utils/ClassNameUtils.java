package com.github.klee0kai.stone.utils;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

public class ClassNameUtils {

    public static ClassName typeOf(String clFullName) {
        if (clFullName.endsWith(".class"))
            clFullName = clFullName.substring(0, clFullName.lastIndexOf(".class"));
        return ClassName.get(clFullName.substring(0, clFullName.lastIndexOf(".")),
                clFullName.substring(clFullName.lastIndexOf(".") + 1));
    }

    public static ClassName genFactoryNameMirror(TypeName or) {
        if (!(or instanceof ClassName)) return null;
        ClassName origin = (ClassName) or;
        return ClassName.get(origin.packageName(), origin.simpleName() + "StoneFactory");
    }


    public static ClassName genModuleNameMirror(TypeName or) {
        if (!(or instanceof ClassName)) return null;
        ClassName origin = (ClassName) or;
        return ClassName.get(origin.packageName(), origin.simpleName() + "StoneModule");
    }

    public static ClassName genInterfaceModuleNameMirror(TypeName or) {
        if (!(or instanceof ClassName)) return null;
        ClassName origin = (ClassName) or;
        return ClassName.get(origin.packageName(), "I" + origin.simpleName() + "StoneModule");
    }


    public static ClassName genComponentNameMirror(TypeName or) {
        if (!(or instanceof ClassName)) return null;
        ClassName origin = (ClassName) or;
        return ClassName.get(origin.packageName(), origin.simpleName() + "StoneComponent");
    }

}
