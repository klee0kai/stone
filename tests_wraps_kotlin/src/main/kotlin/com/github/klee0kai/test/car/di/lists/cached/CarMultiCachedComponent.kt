package com.github.klee0kai.test.car.di.lists.cached

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.wrappers.Ref
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
    fun wheels(): List<Provider<WeakReference<Wheel?>?>?>?
    fun wheel(): Wheel?
    fun windows(): List<List<Window?>?>?
    fun windowsProviding(): List<Provider<List<Window?>?>?>?
    fun cars(): List<Car?>?
}
