package com.github.klee0kai.stone.test_feature.hr.di;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.stone.test_feature.hr.department.SecurityDepartment;
import com.github.klee0kai.stone.test_feature.hr.department.SecurityDepartmentImpl;
import com.github.klee0kai.stone.test_feature.hr.department.SoftwareDepartment;
import com.github.klee0kai.stone.test_feature.hr.department.SoftwareDepartmentImpl;
import com.github.klee0kai.stone.test_feature.hr.store.EmployeesStore;

@Module
public class DepartmentsModule {

    @Provide(cache = Provide.CacheType.Strong)
    public EmployeesStore employeesStore() {
        return new EmployeesStore();
    }


    @Provide(cache = Provide.CacheType.Soft)
    public SecurityDepartment securityDepartment() {
        return new SecurityDepartmentImpl();
    }

    @Provide(cache = Provide.CacheType.Soft)
    public SoftwareDepartment softwareDepartment() {
        return new SoftwareDepartmentImpl();
    }

}
