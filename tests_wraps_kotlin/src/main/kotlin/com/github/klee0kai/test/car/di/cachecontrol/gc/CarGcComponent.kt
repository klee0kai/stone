package com.github.klee0kai.test.car.di.cachecontrol.gc

import com.github.klee0kai.stone.annotations.component.*
import com.github.klee0kai.test.car.di.cachecontrol.gc.scopes.GcBumperRedScope
import com.github.klee0kai.test.car.di.cachecontrol.gc.scopes.GcBumperScope
import com.github.klee0kai.test.car.di.cachecontrol.gc.scopes.GcWheelScope
import com.github.klee0kai.test.car.di.cachecontrol.gc.scopes.GcWindowScope

@Component(identifiers = [String::class, Integer::class])
abstract class CarGcComponent {
    abstract fun bumpersModule(): BumperGcModule?
    abstract fun wheelsModule(): WheelGcModule
    abstract fun wheelMappedModule(): WheelMappedGcModule?
    abstract fun wheelMultiMappedModule(): WheelMultiMappedGcModule
    abstract fun windowsModule(): WindowGcModule?
    abstract fun windowsMappedModule(): WindowMappedGcModule?
    abstract fun windowsMultiMappedModule(): WindowMultiMappedGcModule?

    @GcAllScope
    abstract fun gcAll()

    @GcWeakScope
    abstract fun gcWeak()

    @GcSoftScope
    abstract fun gcSoft()

    @GcStrongScope
    abstract fun gcStrong()

    @GcWheelScope
    abstract fun gcWheels()

    @GcBumperScope
    abstract fun gcBumpers()

    @GcBumperRedScope
    abstract fun gcRedBumpers()

    @GcBumperRedScope
    @GcBumperScope
    abstract fun gcRedBumpers2()

    @GcWindowScope
    abstract fun gcWindows()

    @GcWindowScope
    @GcWeakScope
    abstract fun gcWeakWindows()

    @GcWindowScope
    @GcSoftScope
    abstract fun gcSoftWindows()

    @GcWindowScope
    @GcBumperScope
    abstract fun gcNothing()
    fun gcWindowsAndWheels() {
        gcWindows()
        gcWheels()
    }
}
