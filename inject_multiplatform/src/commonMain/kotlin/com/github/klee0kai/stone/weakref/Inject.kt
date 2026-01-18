package com.github.klee0kai.stone.weakref

/**
 * Identifies injectable constructors, methods, and fields. May apply to static
 * as well as instance members. An injectable member may have any access
 * modifier (private, package-private, protected, public). Constructors are
 * injected first, followed by fields, and then methods. Fields and methods
 * in superclasses are injected before those in subclasses. Ordering of
 * injection among fields and among methods in the same class is not specified.
 */
expect annotation class Inject()