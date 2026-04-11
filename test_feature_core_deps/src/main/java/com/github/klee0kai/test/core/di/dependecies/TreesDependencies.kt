package com.github.klee0kai.test.core.di.dependecies

import com.github.klee0kai.stone.wrappers.AsyncLazy
import com.github.klee0kai.stone.wrappers.LazyProvider
import com.github.klee0kai.test.core.trees.Fir
import com.github.klee0kai.test.core.trees.Palm
import com.github.klee0kai.test.core.trees.Poplar

interface TreesDependencies {

    fun fir(): AsyncLazy<Fir>

    fun palm(): LazyProvider<Palm>

    fun poplar(): LazyProvider<Poplar>

}