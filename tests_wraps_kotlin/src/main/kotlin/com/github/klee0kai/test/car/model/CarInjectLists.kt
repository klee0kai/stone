package com.github.klee0kai.test.car.model

import com.github.klee0kai.stone.annotations.qualifier.IgnoreQualifier
import javax.inject.Inject

class CarInjectLists {

    @Inject
    @IgnoreQualifier
    var bumpers: List<Bumper>? = null

    @Inject
    @IgnoreQualifier
    var wheels: List<Wheel>? = null

    @Inject
    @IgnoreQualifier
    var windows: List<Window>? = null


    var bumpersMethodFrom: List<Bumper>? = null
    var wheelsMethodFrom: List<Wheel>? = null
    var windowsMethodFrom: List<Window>? = null

    @Inject
    fun init(
        @IgnoreQualifier
        bumpers: List<Bumper>?,
        @IgnoreQualifier
        wheels: List<Wheel>?,
        @IgnoreQualifier
        windows: List<Window>?,
    ) {
        bumpersMethodFrom = bumpers
        wheelsMethodFrom = wheels
        windowsMethodFrom = windows
    }
}
