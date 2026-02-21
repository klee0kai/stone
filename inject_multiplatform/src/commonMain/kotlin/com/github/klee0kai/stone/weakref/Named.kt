package com.github.klee0kai.stone.weakref


/**
 * String-based [Qualifier].
 *
 * Example usage:
 *
 * ```
 *   class Car {
 *     @Inject
 *     @Named("driver")
 *     var driverSeat : DriverSeat
 *
 *     @Inject
 *     @Named("passenger")
 *     var passengerSeat: Seat
 *  }
 * ```
 */
annotation class Named(
    val value: String
)