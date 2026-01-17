package com.github.klee0kai.stone.test_feature.consulting.di

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.stone.test_feature.finance.di.AccountingModule
import com.github.klee0kai.stone.test_feature.finance.model.WorkCalendar
import com.github.klee0kai.stone.test_feature.hr.di.DepartmentsModule
import com.github.klee0kai.stone.test_feature.planning.di.ProjectsModule

@Component
interface ConsultingComponent : ConsultingDependencies {

    fun departmentsModule(): DepartmentsModule?

    fun accountingModule(): AccountingModule?

    fun projectsModule(): ProjectsModule?

    @BindInstance(cache = BindInstance.CacheType.Strong)
    fun workCalendar(workCalendar: WorkCalendar?): WorkCalendar?
}
