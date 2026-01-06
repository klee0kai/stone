package com.github.klee0kai.test.car.di.lists.cached

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.annotations.qualifier.IgnoreQualifier
import com.github.klee0kai.stone.weakref.Ref
import com.github.klee0kai.test.car.model.Bumper
import com.github.klee0kai.test.car.model.Car
import com.github.klee0kai.test.car.model.Wheel
import com.github.klee0kai.test.car.model.Window
import java.lang.ref.WeakReference
import javax.inject.Provider

@Component
interface CarMultiCachedComponent {

    fun cachedModule(): CarMultiCachedModule?

    fun singleBumper(): Ref<Bumper?>?

    @IgnoreQualifier
    fun wheels(): List<Provider<WeakReference<Wheel?>?>?>?

    fun wheel(): Wheel?

    @IgnoreQualifier
    fun windows(): List<List<Window?>?>?

    @IgnoreQualifier
    fun windowsProviding(): List<Provider<List<Window?>?>?>?

    fun cars(): List<Car?>?
}
