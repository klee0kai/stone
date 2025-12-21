package com.github.klee0kai.test.boxed.model

import com.github.klee0kai.stone.annotations.qualifier.IgnoreQualifier
import com.github.klee0kai.test.car.model.Bumper
import com.github.klee0kai.test.car.model.Wheel
import com.github.klee0kai.test.car.model.Window
import javax.inject.Inject

class CarBoxedInjectLists {

    @Inject
    @IgnoreQualifier
    var bumpers: List<CarBox<Bumper>>? = null

    @Inject
    @IgnoreQualifier
    var wheels: List<CarBox<Wheel>>? = null

    @Inject
    @IgnoreQualifier
    var windows: List<CarBox<Window>>? = null

    var bumpersMethodFrom: List<CarBox<Bumper>>? = null

    var wheelsMethodFrom: List<CarBox<Wheel>>? = null

    var windowsMethodFrom: List<CarBox<Window>>? = null


    @Inject
    fun init(
        @IgnoreQualifier
        bumpers: List<CarBox<Bumper>>?,

        @IgnoreQualifier
        wheels: List<CarBox<Wheel>>?,

        @IgnoreQualifier
        windows: List<CarBox<Window>>?,
    ) {
        bumpersMethodFrom = bumpers
        wheelsMethodFrom = wheels
        windowsMethodFrom = windows
    }
}
