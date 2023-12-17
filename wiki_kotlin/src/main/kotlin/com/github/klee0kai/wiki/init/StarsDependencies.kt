package com.github.klee0kai.wiki.init

import com.github.klee0kai.stone.annotations.dependencies.Dependencies
import com.github.klee0kai.test.mowgli.galaxy.Sun

@Dependencies
interface StarsDependencies {

    fun sun(): Sun

}
