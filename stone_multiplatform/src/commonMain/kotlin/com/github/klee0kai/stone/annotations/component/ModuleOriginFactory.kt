package com.github.klee0kai.stone.annotations.component

/**
 * Provide origin module's factory
 *
 * <pre>`ㅤ@Component
 * interface AppComponent() {
 *
 * ㅤ@ModuleOriginFactory
 * RepositoriesModule repModuleFactory();
 *
 * }
 *
 * ㅤㅤ@Module
 * interface RepositoriesModule{
 * // some code
 * }
`</pre> *
 *
 *
 * Dependencies can only be initialized on an instance.
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class ModuleOriginFactory 
