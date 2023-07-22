package com.github.klee0kai.test.car.di.bindinstance.simple

import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.test.car.model.Bumper
import com.github.klee0kai.test.car.model.Wheel
import com.github.klee0kai.test.car.model.Window
import java.lang.ref.WeakReference

@Module
interface CarBindModule {
    @BindInstance
    fun wheel(): Wheel

    @BindInstance
    fun bumper(): WeakReference<Bumper>

    @BindInstance
    fun windows(): WeakReference<List<Window>>
}
