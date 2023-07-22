package com.github.klee0kai.test.car.di.qualifiers.qualifiers

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class MyQualifierMulti(val id: String = "", val indx: Int = 0, val type: Type = Type.SIMPLE) {
    enum class Type {
        SIMPLE,
        MIDDLE,
        HARD
    }
}
