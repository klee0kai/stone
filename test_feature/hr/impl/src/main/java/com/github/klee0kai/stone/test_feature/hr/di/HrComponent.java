package com.github.klee0kai.stone.test_feature.hr.di;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.component.Init;
import com.github.klee0kai.stone.test_feature.hr.store.EmployeesStore;

@Component
public interface HrComponent {

    DepartmentsModule departmentsModule();

    HrDependencies hrDependencies();

    EmployeesStore employeesStore();

    @Init
    void initDeps(HrDependencies deps);

}
