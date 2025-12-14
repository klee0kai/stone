package com.github.klee0kai.test_ext.inject.di.gcscopes

import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class GcSiriusScope 
