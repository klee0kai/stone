package com.github.klee0kai.stone.test_feature.hr.di;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.interfaces.IComponent;
import com.github.klee0kai.stone.test_feature.hr.store.EmployeesStore;

@Component
public interface HrComponent extends IComponent {

    DepartmentsModule departmentsModule();

    HrDependencies hrDependencies();

    EmployeesStore employeesStore();

}
