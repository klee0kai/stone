package com.github.klee0kai.stone.annotations.component;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Initializing a module or dependency in a component.
 * <p>
 * An initialization method is declared in a component that accepts an instance of the module or its class.
 *
 * <pre>{@code
 *    ㅤ@Component
 *     interface AppComponent() {
 *
 *        ㅤ@Init
 *         void initRepositoriesModule(RepositoriesModule module);
 *
 *        ㅤ@Init
 *         void initRepositoriesModule(Class<? extends RepositoriesModule> module);
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
public @interface Init {
}
