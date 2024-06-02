package com.github.klee0kai.test.app.di.module

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test.app.mushrooms.Amanita
import com.github.klee0kai.test.app.mushrooms.Cep
import com.github.klee0kai.test.app.mushrooms.Champignon
import com.github.klee0kai.test.app.mushrooms.Russule

@Module
interface MushroomsModule {

    @Provide(cache = Provide.CacheType.Weak)
    fun cep(): Cep

    @Provide(cache = Provide.CacheType.Weak)
    fun amanita(): Amanita

    @Provide(cache = Provide.CacheType.Weak)
    fun campignon(): Champignon

    @Provide(cache = Provide.CacheType.Weak)
    fun russule(): Russule

}