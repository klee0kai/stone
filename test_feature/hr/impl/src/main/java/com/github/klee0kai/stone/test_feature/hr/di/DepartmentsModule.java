package com.github.klee0kai.stone.test_feature.hr.di;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.stone.test_feature.hr.department.SecurityDepartment;
import com.github.klee0kai.stone.test_feature.hr.department.SecurityDepartmentImpl;
import com.github.klee0kai.stone.test_feature.hr.department.SoftwareDepartment;
import com.github.klee0kai.stone.test_feature.hr.department.SoftwareDepartmentImpl;

@Module
public class DepartmentsModule {

    @Provide
    public SecurityDepartment securityDepartment() {
        return new SecurityDepartmentImpl();
    }

    @Provide
    public SoftwareDepartment softwareDepartment() {
        return new SoftwareDepartmentImpl();
    }

}
