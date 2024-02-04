package com.github.klee0kai.wiki.provide.identifiers

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class MyQualifier


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class ThreadQualifier(val type: ThreadType = ThreadType.Main) {
    enum class ThreadType {
        Main,
        Default,
        IO
    }
}
