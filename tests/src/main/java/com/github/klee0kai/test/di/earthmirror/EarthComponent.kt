package com.github.klee0kai.test.di.earthmirror

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.test.mowgli.earth.Cave.CaveType

@Component(identifiers = [CaveType::class, Int::class])
interface EarthComponent {
    fun east(): EastModule?

    fun west(): WestModule?
}
