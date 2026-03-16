package com.github.klee0kai.stone.test_feature.finance.model

import java.util.*

class WorkCalendar(
    val name: String?,
    val workDaysInYear: Int,
    val workHoursInDay: Int,
) {
    val uuid: String = UUID.randomUUID().toString()
}
