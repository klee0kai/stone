package com.github.klee0kai.stone.annotations.component;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Garbage collection in the selected osprey.
 * We explicitly clean up unused objects in DI.
 * <p>
 * In fact, only those objects that are used and not held by anyone will be deleted.
 * <pre>{@code
 *    ㅤ@Component
 *     interface AppComponent {
 *
 *        ㅤ@GcAllScope
 *        ㅤ@AuthScope
 *        ㅤ@RunGc
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
 * <p>
 * Caching will be cleared only for those objects that are not actually used in the application and are not held by anyone.
 * If you hold on to the provided and cached object when calling this method, it will not be destroyed.
 * Objects cached by a Weak link will almost always be collected when not in use.
 */

@Retention(value = RetentionPolicy.CLASS)
@Target(value = ElementType.METHOD)
public @interface RunGc {


}
