package com.github.klee0kai.stone.annotations.module;


import java.lang.annotation.*;

/**
 * Bind external dependencies
 */
@Retention(value = RetentionPolicy.CLASS)
@Target(value = ElementType.METHOD)
@Inherited
public @interface BindInstance {

    enum CacheType {
        Weak,
        Soft,
        Strong
    }

    BindInstance.CacheType cache() default BindInstance.CacheType.Soft;

    /**
     * Alternative providing types
     */
    Class[] alternatives() default {};

}
