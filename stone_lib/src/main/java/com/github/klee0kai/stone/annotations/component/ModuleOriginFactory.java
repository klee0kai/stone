package com.github.klee0kai.stone.annotations.component;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provide origin module's factory
 *
 * <pre>{@code
 *    ㅤ@Component
 *     interface AppComponent() {
 *
 *        ㅤ@ModuleOriginFactory
 *         RepositoriesModule repModuleFactory();
 *
 *     }
 *
 *  ㅤㅤ@Module
 *     interface RepositoriesModule{
 *         // some code
 *     }
 * }</pre>
 * <p>
 * Dependencies can only be initialized on an instance.
 */
@Retention(value = RetentionPolicy.CLASS)
@Target(value = ElementType.METHOD)
public @interface ModuleOriginFactory {
}
