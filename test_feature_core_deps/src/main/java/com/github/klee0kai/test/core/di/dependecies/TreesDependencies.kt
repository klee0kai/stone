package com.github.klee0kai.test.core.di.dependecies

import com.github.klee0kai.stone.wrappers.AsyncProvide
import com.github.klee0kai.stone.wrappers.LazyProvide
import com.github.klee0kai.test.core.trees.Fir
import com.github.klee0kai.test.core.trees.Palm
import com.github.klee0kai.test.core.trees.Poplar

interface TreesDependencies {

    fun fir(): AsyncProvide<Fir>

    fun palm(): LazyProvide<Palm>

    fun poplar(): LazyProvide<Poplar>

}