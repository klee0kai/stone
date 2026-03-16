package com.github.klee0kai.stone.test_feature.planning

import com.github.klee0kai.stone.test_feature.finance.model.WorkCalendar
import com.github.klee0kai.stone.test_feature.hr.department.SecurityDepartment
import com.github.klee0kai.stone.test_feature.planning.di.PlanningComponent
import com.github.klee0kai.stone.test_feature.planning.project.LogisticProject
import javax.inject.Inject

class PlanningRun(private val component: PlanningComponent) {

    @Inject
    var workCalendar: WorkCalendar? = null

    @Inject
    var logisticProject: LogisticProject? = null

    @Inject
    var securityDepartment: SecurityDepartment? = null

    fun start() {
        component.inject(this)
    }
}
