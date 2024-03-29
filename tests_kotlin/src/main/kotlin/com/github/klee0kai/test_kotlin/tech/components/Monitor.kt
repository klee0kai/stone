package com.github.klee0kai.test_kotlin.tech.components

import com.github.klee0kai.test_kotlin.di.base_comp.identifiers.Company
import com.github.klee0kai.test_kotlin.di.base_comp.identifiers.MonitorSize
import java.util.*


class Monitor @JvmOverloads constructor(
    val size: MonitorSize = MonitorSize("11"),
    val company: Company = Company("samsung")
) {

    val uuid = UUID.randomUUID()


}