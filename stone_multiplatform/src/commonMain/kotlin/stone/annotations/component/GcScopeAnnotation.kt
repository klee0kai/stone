package stone.annotations.component

/**
 * Annotation announcing new scopes for garbage collection and changing the caching method.
 * Announce new scopes in the following way.
 *
 * <pre>`ㅤ@GcScopeAnnotation
 * ㅤ@Retention(RUNTIME)
 * ㅤ@Target(METHOD)
 * public @interface GcPlanetScope {
 * }
`</pre> *
 * Instead of this annotation, you can use the more general [javax.inject.Scope]
 * annotation.
 *
 * <pre>`ㅤ@Scope
 * ㅤ@Retention(RUNTIME)
 * ㅤ@Target(METHOD)
 * public @interface GcPlanetScope {
 * }
`</pre> *
 *
 *
 * Not Follow [javax.inject.Scope] documentation.
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class GcScopeAnnotation 
