package com.github.klee0kai.stone;

import java.lang.reflect.InvocationTargetException;

public class Stone {

    /**
     * Create a new Stone component
     */
    public static <T> T createComponent(Class<T> component) {
        try {
            Class<?> gennedClass = Class.forName(component.getCanonicalName() + "StoneComponent");
            return (T) gennedClass.getConstructors()[0].newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }



}
