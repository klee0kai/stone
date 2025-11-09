package stone.wrappers

import javax.inject.Provider

fun interface Ref<T> : javax.inject.Provider<T> {

    override fun get(): T

}

