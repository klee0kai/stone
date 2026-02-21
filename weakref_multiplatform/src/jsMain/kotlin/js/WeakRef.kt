package js

external class WeakRef<T : Any?>(target: T) {
    fun deref(): T?
}