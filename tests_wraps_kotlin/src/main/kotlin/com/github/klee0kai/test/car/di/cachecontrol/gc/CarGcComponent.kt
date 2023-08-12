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

    @RunGc
    @GcAllScope
    abstract fun gcAll()

    @RunGc
    @GcWeakScope
    abstract fun gcWeak()

    @RunGc
    @GcSoftScope
    abstract fun gcSoft()

    @RunGc
    @GcStrongScope
    abstract fun gcStrong()

    @RunGc
    @GcWheelScope
    abstract fun gcWheels()

    @RunGc
    @GcBumperScope
    abstract fun gcBumpers()

    @RunGc
    @GcBumperRedScope
    abstract fun gcRedBumpers()

    @RunGc
    @GcBumperRedScope
    @GcBumperScope
    abstract fun gcRedBumpers2()

    @RunGc
    @GcWindowScope
    abstract fun gcWindows()

    @RunGc
    @GcWindowScope
    @GcWeakScope
    abstract fun gcWeakWindows()

    @RunGc
    @GcWindowScope
    @GcSoftScope
    abstract fun gcSoftWindows()

    @RunGc
    @GcWindowScope
    @GcBumperScope
    abstract fun gcNothing()


    fun gcWindowsAndWheels() {
        gcWindows()
        gcWheels()
    }
}
