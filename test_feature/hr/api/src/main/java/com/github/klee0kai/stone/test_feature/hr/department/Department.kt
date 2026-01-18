package com.github.klee0kai.stone.test_feature.hr.department

import com.github.klee0kai.stone.test_feature.hr.model.DepartmentInfo
import com.github.klee0kai.stone.test_feature.hr.model.EmployeeInfo

interface Department {
    val id: String?

    val info: DepartmentInfo?

    fun employers(): MutableList<EmployeeInfo?>?

    fun hireEmployee(employeeInfo: EmployeeInfo?)

    fun fireEmployee(employeeInfo: EmployeeInfo?)
}
