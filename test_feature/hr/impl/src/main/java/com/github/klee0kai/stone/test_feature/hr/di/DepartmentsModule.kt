package com.github.klee0kai.stone.test_feature.hr.di

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.stone.test_feature.hr.department.SecurityDepartment
import com.github.klee0kai.stone.test_feature.hr.department.SecurityDepartmentImpl
import com.github.klee0kai.stone.test_feature.hr.department.SoftwareDepartment
import com.github.klee0kai.stone.test_feature.hr.department.SoftwareDepartmentImpl
import com.github.klee0kai.stone.test_feature.hr.store.EmployeesStore

@Module
interface DepartmentsModule {

    @Provide(cache = Provide.CacheType.Strong)
    fun employeesStore(): EmployeesStore {
        return EmployeesStore()
    }


    @Provide(cache = Provide.CacheType.Soft)
    fun securityDepartment(): SecurityDepartment {
        return SecurityDepartmentImpl()
    }

    @Provide(cache = Provide.CacheType.Soft)
    fun softwareDepartment(): SoftwareDepartment {
        return SoftwareDepartmentImpl()
    }
}
