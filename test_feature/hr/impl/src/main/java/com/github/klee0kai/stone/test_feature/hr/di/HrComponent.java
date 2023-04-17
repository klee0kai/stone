package com.github.klee0kai.stone.test_feature.hr.di;

import com.github.klee0kai.stone.annotations.component.Component;

@Component
public interface HrComponent {

    DepartmentsModule departmentsModule();

    HrDependencies hrDependencies();

}
