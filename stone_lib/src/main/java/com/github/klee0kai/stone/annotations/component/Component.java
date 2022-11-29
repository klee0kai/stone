package com.github.klee0kai.stone.annotations.component;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Dependency injection component.
 * <p>
 * <p>
 * Provide modules.
 * Inject items to classes.
 * Gc run and protect.
 * Bind instance items.
 */
@Retention(value = RetentionPolicy.CLASS)
@Target(value = ElementType.TYPE)
public @interface Component {

    /**
     * Qualifiers, which using in inject methods
     *
     * @return
     */
    Class[] qualifiers() default {};

}
