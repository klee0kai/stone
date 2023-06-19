package com.github.klee0kai.test_kotlin.di.compfactory

import com.github.klee0kai.stone.types.wrappers.Ref
import com.github.klee0kai.stone.types.wrappers.LazyProvide
import com.github.klee0kai.stone.types.wrappers.PhantomProvide
import com.github.klee0kai.test_kotlin.tech.components.Monitor
import java.lang.ref.SoftReference
import java.lang.ref.WeakReference
import javax.inject.Provider


interface ICompFactoryWrappersComponent {

    fun monitorLazy(): LazyProvide<Monitor>
    fun monitorProviderIRef(): Ref<Monitor>
    fun monitorPhantomProvide(): PhantomProvide<Monitor>
    fun monitorProvider(): Provider<Monitor>
    fun monitorSoft(): SoftReference<Monitor>
    fun monitorWeak(): WeakReference<Monitor>

    fun monitorLazyDelegate(): Lazy<Monitor>


}