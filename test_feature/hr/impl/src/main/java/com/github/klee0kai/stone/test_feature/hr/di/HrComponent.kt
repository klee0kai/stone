package com.github.klee0kai.stone.test_feature.hr.di

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.annotations.component.Init
import com.github.klee0kai.stone.test_feature.hr.store.EmployeesStore

@Component
interface HrComponent {
    fun departmentsModule(): DepartmentsModule?

    fun hrDependencies(): HrDependencies?

    fun employeesStore(): EmployeesStore?

    @Init
    fun initDeps(deps: HrDependencies?)
}
