package com.github.klee0kai.test.core.di.dependecies

import com.github.klee0kai.test.core.forest.Alder
import com.github.klee0kai.test.core.forest.Ash
import com.github.klee0kai.test.core.forest.Beech

interface CoreDepsProvider : BirdsDependencies, TreesDependencies {

    fun alder(): Alder

    fun ash(): Ash

    fun beech(): Beech

}
