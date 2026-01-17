package com.github.klee0kai.stone.test_feature.hr.department

import com.github.klee0kai.stone.test_feature.hr.model.DepartmentInfo
import com.github.klee0kai.stone.test_feature.hr.model.EmployeeInfo
import java.util.*

open class DepartmentImpl : Department {
    var uuid: String? = UUID.randomUUID().toString()

    override val id: String? get() = uuid

    override val info: DepartmentInfo? get() = null

    override fun employers(): MutableList<EmployeeInfo?>? {
        return null
    }

    override fun hireEmployee(employeeInfo: EmployeeInfo?) {
    }

    override fun fireEmployee(employeeInfo: EmployeeInfo?) {
    }
}
