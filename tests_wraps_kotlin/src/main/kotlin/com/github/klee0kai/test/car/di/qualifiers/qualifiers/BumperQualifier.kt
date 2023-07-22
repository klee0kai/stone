package com.github.klee0kai.test.car.di.qualifiers.qualifiers

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class BumperQualifier(val type: BumperType = BumperType.Simple) {
    enum class BumperType {
        Simple,
        Reinforced
    }
}
