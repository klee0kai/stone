package com.github.klee0kai.test.car.di.bindinstance.simple

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.stone.wrappers.Ref
import com.github.klee0kai.test.car.model.Bumper
import com.github.klee0kai.test.car.model.Wheel
import com.github.klee0kai.test.car.model.Window
import java.lang.ref.Reference
import java.lang.ref.WeakReference

@Component
interface CarBindComponent {
    fun module(): CarBindModule

    @BindInstance
    fun bindWheel(wheel: Wheel?)

    @BindInstance
    fun bindWheelRef(wheel: Reference<Wheel?>?)

    @BindInstance
    fun bindBumper(bumper: Ref<Bumper>?)

    @BindInstance
    fun bindWindow(window: Window?)

    @BindInstance
    fun bindWindows(window: Collection<Window?>)

    @BindInstance
    fun bindWindowRefs(window: Collection<WeakReference<Window>>)

    fun provideWheel(): Wheel
    fun provideWheelRef(): Reference<Wheel?>?
    fun provideWheels(): List<Reference<Wheel?>?>?
    fun provideBumper(): Ref<Bumper?>?
    fun provideBumpers(): List<Bumper?>?
    fun provideWindow(): Window
    fun provideWindows(): Collection<Window?>?
}
