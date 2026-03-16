package js

external class WeakRef<T : JsAny>(target: T) {
    fun deref(): T?
}