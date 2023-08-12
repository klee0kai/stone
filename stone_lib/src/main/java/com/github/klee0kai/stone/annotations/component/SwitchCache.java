package com.github.klee0kai.stone.annotations.component;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Switch cache for scoped items.
 * <p>
 * Work one time. After reset cache, cache work in default mode.
 */

@Retention(value = RetentionPolicy.CLASS)
@Target(value = ElementType.METHOD)
public @interface SwitchCache {

    enum CacheType {
        /**
         * restore Default config cache
         */
        Default,
        /**
         * reset cache
         */
        Reset,
        /**
         * switch cache to weak.
         */
        Weak,
        /**
         * switch cache to soft.
         */
        Soft,
        /**
         * switch cache to strong.
         */
        Strong
    }

    /**
     * new cache type for providing items
     *
     */
    CacheType cache() default CacheType.Default;

    /**
     * Switch cache time duration.
     * After time cache restored to default.
     *
     */
    long timeMillis() default -1;

}
