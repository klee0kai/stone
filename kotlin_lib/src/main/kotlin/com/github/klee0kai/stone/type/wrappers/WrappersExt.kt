package com.github.klee0kai.stone.type.wrappers

import com.github.klee0kai.stone.types.wrappers.IRef
import java.lang.ref.SoftReference
import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

operator fun <T> IRef<T>.getValue(t: Any?, property: KProperty<*>): T = get()

operator fun <T> SoftReference<T>.getValue(t: Any?, property: KProperty<*>): T? = get()

operator fun <T> WeakReference<T>.getValue(t: Any?, property: KProperty<*>): T? = get()

