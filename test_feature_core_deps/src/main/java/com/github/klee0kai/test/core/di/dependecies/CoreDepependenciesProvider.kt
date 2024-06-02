package com.github.klee0kai.test.core.di.dependecies

import com.github.klee0kai.stone.annotations.dependencies.Dependencies
import com.github.klee0kai.test.core.forest.Alder
import com.github.klee0kai.test.core.forest.Ash
import com.github.klee0kai.test.core.forest.Beech

@Dependencies
interface CoreDepependenciesProvider : BirdsDependencies, TreesDependencies {

    fun alder(): Alder

    fun ash(): Ash

    fun beech(): Beech

}
