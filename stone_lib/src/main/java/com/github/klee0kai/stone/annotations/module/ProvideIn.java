package com.github.klee0kai.stone.annotations.module;


import com.github.klee0kai.stone.annotations.module.Provide;

import java.lang.annotation.*;

/**
 * Providing component outsize module.
 */
@Retention(value = RetentionPolicy.CLASS)
@Target(value = ElementType.TYPE)
@Inherited
public @interface ProvideIn {

    /**
     * Providing module
     * @return
     */
    Class module();


    enum CacheType {
        FACTORY,
        WEAK,
        SOFT,
        STRONG
    }

    Provide.CacheType cache() default Provide.CacheType.SOFT;

}
