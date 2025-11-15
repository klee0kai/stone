package com.github.klee0kai.stone.wrappers

import com.github.klee0kai.stone.weakref.Ref

/**
 * Providing an object without any deduction.
 * Each time you use this object through a provider,
 * the object is not cached in your class, but is taken from the component each time.
 *
 *
 * Together with the use of Kotlin delegates, you can fully use the objects from the
 * DI component directly without keeping them from being deleted in your class.
 * <pre>`ㅤ@Component
 * interface Component {
 *
 * PhantomProvide<WelcomePresenter> presenter();
 *
 * }
`</pre> *
 */
class PhantomProvide<T>(private val call: Ref<T?>) : Ref<T?> {

    override fun get(): T? {
        return call.get()
    }

}
