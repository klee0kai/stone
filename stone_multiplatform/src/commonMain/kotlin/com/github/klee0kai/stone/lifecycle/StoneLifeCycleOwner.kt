package com.github.klee0kai.stone.lifecycle

/**
 * Objects in an application can have a life cycle.
 * Various components of your application can be created, destroyed, and re-created.
 *
 *
 * At the time of re-creation of the object, it may be necessary to save the objects provided to it from DI.
 * In a normal case, this can be done by declaring an additional `@ProtectInjected` method in the component.
 * For example:
 * <pre>`ㅤ@Component
 * interface AppComponent {
 *
 * void inject(Activity activity);
 *
 * ㅤ@ProtectInjected
 * void protectInjected(Activity activity);
 *
 * }
`</pre> *
 *
 *
 * If your class can track life cycle events.
 * Then for such classes, you can implement the Stone life cycle,
 * which independently calls the deletion protection methods.
 *
 *
 * For example, for Android Activity it will look like this.
 * <pre>`public static StoneLifeCycleOwner lifeCycleOwner(Lifecycle lifecycle, long protectTimeMillis) {
 * return listener -> lifecycle.addObserver(new DefaultLifecycleObserver() {
 * ㅤ@Override
 * public void onPause(@NonNull @NotNull LifecycleOwner owner1) {
 * DefaultLifecycleObserver.super.onPause(owner1);
 * listener.protectForInjected(protectTimeMillis);
 * }
 * });
 * }
`</pre> *
 *
 *
 * Further, you simply use your class when injecting itself (if it implements StoneLifeCycleOwner),
 * or with an additional StoneLifeCycleOwner argument
 *
 * <pre>`ㅤ@Component
 * interface AppComponent {
 *
 * void inject(Activity activity, StoneLifeCycleOwner owner);
 *
 * }
`</pre> *
 */
fun interface StoneLifeCycleOwner {

    fun subscribe(listener: StoneLifeCycleListener)

}
