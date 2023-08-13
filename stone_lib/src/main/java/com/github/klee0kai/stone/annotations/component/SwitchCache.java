package com.github.klee0kai.stone.annotations.component;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Changing the caching method.
 * Changes the caching method for cached objects, temporarily or permanently.
 * <p>
 * Allows in this way either to speed up the cleaning of unused objects from memory, or to protect them from deletion.
 *
 * <pre>{@code
 *    ㅤ@Component
 *     interface AppComponent {
 *
 *        ㅤ@GcAllScope
 *        ㅤ@AuthScope
 *        ㅤ@SwitchCache(cache = SwitchCache.CacheType.Weak)
 *         public void authWeak();
 *
 *     }
 *
 *    ㅤ@GcScopeAnnotation
 *    ㅤ@Retention(value = RetentionPolicy.CLASS)
 *    ㅤ@Target(value = ElementType.METHOD)
 *     public @interface AuthScope {
 *     }
 * }</pre>
 * <p>
 * Can be used with multiple garbage collector scopes.
 * The final scope will be the intersection of several.
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
