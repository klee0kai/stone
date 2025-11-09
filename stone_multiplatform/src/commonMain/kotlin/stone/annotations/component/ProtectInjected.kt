package stone.annotations.component

/**
 * Protect provided objects from being destroyed by injection.
 * When a dependency consumer class is re-created, cached objects may be garbage collected.
 * This can be prevented by calling the deletion protection method.
 * <pre>`ㅤ@Component
 * public interface AppComponent {
 *
 *
 * void inject(Activity activity);
 *
 * ㅤ@ProtectInjected
 * void protectInjected();
 *
 * }
`</pre> *
 *
 *
 * Deletion protection changes object caching to strict. Objects cannot be deleted for some time.
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class ProtectInjected(
    /**
     * protect time millis.
     * Default protect 5 sec.
     */
    val timeMillis: Long = 5000L
)
