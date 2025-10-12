package com.github.klee0kai.stone.wrappers

/**
 * Providing an object with lazy providing.
 * The object will be provided the first time it is used.
 * <pre>`ㅤ@Component
 * interface Component {
 *
 * LazyProvide<WelcomePresenter> presenter();
 *
 * }
`</pre> *
 */
class LazyProvide<T>(private val call: Ref<T?>) : Ref<T?> {
    private var value: T? = null

    override fun get(): T? {
        if (value != null) return value
        return call.get().also { value = it }
    }
}
