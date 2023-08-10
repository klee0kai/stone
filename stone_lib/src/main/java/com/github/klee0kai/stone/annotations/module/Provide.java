package com.github.klee0kai.stone.annotations.module;


import java.lang.annotation.*;

/**
 * Provide method in module.
 * Could be ignored, behavior by default.
 */
@Retention(value = RetentionPolicy.CLASS)
@Target(value = ElementType.METHOD)
@Inherited
public @interface Provide {

    enum CacheType {
        Factory,
        Weak,
        Soft,
        Strong
    }

    CacheType cache() default CacheType.Factory;

    /**
     * Alternative providing types
     */
    Class[] alternatives() default {};

}
