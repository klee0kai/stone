package com.github.klee0kai.stone.annotations.component;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Protect provided objects from being destroyed by injection.
 * When a dependency consumer class is re-created, cached objects may be garbage collected.
 * This can be prevented by calling the deletion protection method.
 * <pre>{@code
 *   ㅤ@Component
 *     public interface AppComponent {
 *
 *
 *         void inject(Activity activity);
 *
 *        ㅤ@ProtectInjected
 *         void protectInjected();
 *
 *     }
 * }</pre>
 * <p>
 * Deletion protection changes object caching to strict. Objects cannot be deleted for some time.
 */

@Retention(value = RetentionPolicy.CLASS)
@Target(value = ElementType.METHOD)
public @interface ProtectInjected {

    /**
     * protect time millis.
     * Default protect 5 sec.
     */
    long timeMillis() default 5000L;

}
