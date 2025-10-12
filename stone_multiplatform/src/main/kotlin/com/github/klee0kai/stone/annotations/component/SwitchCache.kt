package com.github.klee0kai.stone.annotations.component


/**
 * Changing the caching method.
 * Changes the caching method for cached objects, temporarily or permanently.
 *
 *
 * Allows in this way either to speed up the cleaning of unused objects from memory, or to protect them from deletion.
 *
 * <pre>`ㅤ@Component
 * interface AppComponent {
 *
 * ㅤ@GcAllScope
 * ㅤ@AuthScope
 * ㅤ@SwitchCache(cache = SwitchCache.CacheType.Weak)
 * public void authWeak();
 *
 * }
 *
 * ㅤ@GcScopeAnnotation
 * ㅤ@Retention(value = RetentionPolicy.CLASS)
 * ㅤ@Target(value = ElementType.METHOD)
 * public @interface AuthScope {
 * }
`</pre> *
 *
 *
 * Can be used with multiple garbage collector scopes.
 * The final scope will be the intersection of several.
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class SwitchCache(
    /**
     * new cache type for providing items
     *
     */
    val cache: CacheType = CacheType.Default,
    /**
     * Switch cache time duration.
     * After time cache restored to default.
     *
     */
    val timeMillis: Long = -1
) {
    enum class CacheType {
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
}
