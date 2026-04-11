package com.github.klee0kai.stone.lifecycle

/**
 * Enables automatic injection protection tied to a consumer's lifecycle.
 *
 * Application components (e.g., Android Activities, Fragments) can be destroyed and
 * re-created. During re-creation, injected objects might be garbage-collected before
 * the new instance can receive them. `StoneLifeCycleOwner` automates the protection
 * process by subscribing to lifecycle events and calling
 * [StoneLifeCycleListener.protectForInjected] at the right moments.
 *
 * ---
 *
 * ## Android Activity example
 *
 * Create a lifecycle owner that protects injected objects when the activity pauses:
 *
 * ```kotlin
 * fun lifeCycleOwner(lifecycle: Lifecycle, protectTimeMillis: Long): StoneLifeCycleOwner {
 *     return StoneLifeCycleOwner { listener ->
 *         lifecycle.addObserver(object : DefaultLifecycleObserver {
 *             override fun onPause(owner: LifecycleOwner) {
 *                 listener.protectForInjected(protectTimeMillis)
 *             }
 *         })
 *     }
 * }
 * ```
 *
 * ---
 *
 * ## Using with injection
 *
 * Pass the lifecycle owner as an additional parameter to the inject method:
 *
 * ```kotlin
 * @Component
 * interface ForestComponent {
 *     fun inject(horse: Horse?, stoneLifeCycleOwner: StoneLifeCycleOwner?)
 *     fun inject(horse: Horse?)
 * }
 * ```
 *
 * ---
 *
 * ## With identifiers
 *
 * Lifecycle owners can be combined with identifiers in injection methods:
 *
 * ```kotlin
 * @Component(identifiers = [ScreenId::class, LoginId::class])
 * interface AppComponent {
 *     fun inject(screen: FeatureScreen, owner: StoneLifeCycleOwner, loginId: LoginId, screenId: ScreenId)
 * }
 * ```
 *
 * ---
 *
 * ## Nuances
 *
 * - If the consumer class itself implements `StoneLifeCycleOwner`, it can be
 *   passed directly — no additional parameter is needed.
 * - The lifecycle owner is called once during injection to subscribe to events.
 *   Subsequent lifecycle events (e.g., `onPause`) trigger protection automatically.
 * - This is an alternative to explicitly calling `@ProtectInjected` methods.
 *
 * @see StoneLifeCycleListener
 * @see com.github.klee0kai.stone.annotations.component.ProtectInjected
 */
fun interface StoneLifeCycleOwner {

    /**
     * Subscribes to lifecycle events to receive protection callbacks.
     *
     * Called by Stone during injection. The implementation should register
     * the [listener] with the platform's lifecycle mechanism and invoke
     * [StoneLifeCycleListener.protectForInjected] at the appropriate moments.
     *
     * @param listener the listener that triggers injection protection
     */
    fun subscribe(listener: StoneLifeCycleListener)

}
