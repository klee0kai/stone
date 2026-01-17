package com.github.klee0kai.stone.test_feature.planning.di;

import com.github.klee0kai.stone.annotations.dependencies.Dependencies;
import com.github.klee0kai.stone.test_feature.finance.model.WorkCalendar;
import com.github.klee0kai.stone.test_feature.hr.department.SecurityDepartment;

@Dependencies
public interface PlanningDependencies {

    WorkCalendar workCalendar();

    SecurityDepartment securityDepartment();

}
