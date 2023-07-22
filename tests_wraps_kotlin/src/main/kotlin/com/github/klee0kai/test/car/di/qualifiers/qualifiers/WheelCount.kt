package com.github.klee0kai.test.car.di.qualifiers.qualifiers

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class WheelCount(val count: Int = 1)
