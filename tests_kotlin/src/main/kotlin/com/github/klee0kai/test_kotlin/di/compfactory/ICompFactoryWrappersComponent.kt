package com.github.klee0kai.test_kotlin.di.compfactory

import com.github.klee0kai.stone.weakref.Ref
import com.github.klee0kai.stone.wrappers.LazyProvider
import com.github.klee0kai.stone.Provider
import com.github.klee0kai.test_kotlin.tech.components.Monitor
import java.lang.ref.SoftReference
import java.lang.ref.WeakReference


interface ICompFactoryWrappersComponent {

    fun monitorLazy(): LazyProvider<Monitor>
    fun monitorProviderIRef(): Ref<Monitor>
    fun monitorPhantomProvide(): Provider<Monitor>
    fun monitorProvider(): Provider<Monitor>
    fun monitorSoft(): SoftReference<Monitor>
    fun monitorWeak(): WeakReference<Monitor>

    fun monitorLazyDelegate(): Lazy<Monitor>


}