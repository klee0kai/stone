package com.github.klee0kai.stone.lifecycle

/**
 * A listener that receives lifecycle-related protection events from Stone DI.
 *
 * When a [StoneLifeCycleOwner] detects that a consumer is about to be
 * re-created (e.g., an Android Activity going through `onPause`), it calls
 * [protectForInjected] to temporarily protect injected objects from garbage collection.
 *
 * This interface is typically not implemented directly by user code — it is
 * created internally by Stone and passed to [StoneLifeCycleOwner.subscribe].
 *
 * @see StoneLifeCycleOwner
 * @see com.github.klee0kai.stone.annotations.component.ProtectInjected
 */
fun interface StoneLifeCycleListener {

    /**
     * Temporarily protects all injected objects from garbage collection.
     *
     * @param timeMillis duration in milliseconds for which the protection is active
     */
    fun protectForInjected(timeMillis: Long)

}
